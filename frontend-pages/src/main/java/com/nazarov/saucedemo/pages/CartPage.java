package com.nazarov.saucedemo.pages;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.pages.base.BasePage;
import com.nazarov.saucedemo.pages.components.CartItemComponent;
import com.nazarov.saucedemo.pages.components.FooterComponent;
import com.nazarov.saucedemo.pages.components.HeaderComponent;
import io.qameta.allure.Step;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class CartPage extends BasePage {

  // Components
  private final HeaderComponent header;
  private final FooterComponent footer;

  // Component locators
  private final Locator headerLocator = page.locator("#header_container");
  private final Locator footerLocator = page.locator("footer.footer");

  // Page locators
  private final Locator pageTitle = page.locator(".title");
  private final Locator continueShoppingButton = page.locator("[data-test='continue-shopping']");
  private final Locator checkoutButton = page.locator("[data-test='checkout']");
  private final Locator cartListContainer = page.locator(".cart_list");
  private final Locator cartItemContainers = cartListContainer.locator(".cart_item");

  /**
   * Constructor for CartPage.
   * @param page The Playwright Page instance.
   */
  public CartPage(Page page) {
    super(page);
    this.header = new HeaderComponent(this.page, this.headerLocator);
    this.footer = new FooterComponent(this.page, this.footerLocator);
    log.info("CartPage object created for Page: {}", page.hashCode());
  }

  @Override
  @Step("Opening Cart Page directly")
  public CartPage open() {
    navigateTo("/cart.html");
    return verifyLoaded();
  }

  @Override
  @Step("Verifying Cart Page essential elements are present")
  public CartPage verifyLoaded() {
    log.info("Verifying Cart Page essential elements are present");
    assertThat(page.url()).as("Cart Page URL check").contains("/cart.html");
    assertThat(pageTitle.isVisible()).as("Page title 'Your Cart' should be present").isTrue();
    assertThat(pageTitle.textContent()).as("Page title text check").isEqualToIgnoringCase("Your Cart");
    assertThat(continueShoppingButton.isVisible()).as("Continue Shopping button should be present").isTrue();
    assertThat(checkoutButton.isVisible()).as("Checkout button should be present").isTrue();
    assertThat(cartListContainer.isVisible()).as("Cart list container should be present").isTrue();
    assertThat(header.isVisible()).as("Header component should be present").isTrue();
    assertThat(footer.isVisible()).as("Footer component should be present").isTrue();
    return this;
  }

  @Step("Clicking 'Continue Shopping' button")
  public InventoryPage clickContinueShopping() {
    log.info("Clicking Continue Shopping button");
    continueShoppingButton.click();
    return new InventoryPage(this.page);
  }

  @Step("Clicking 'Checkout' button")
  public CheckoutStepOnePage clickCheckout() {
    log.info("Clicking Checkout button");
    checkoutButton.click();
    return new CheckoutStepOnePage(this.page);
  }

  /**
   * Retrieves a list of CartItemComponent instances for all items currently in the cart.
   * Returns an empty list if the cart is empty.
   * @return List of CartItemComponent.
   */
  @Step("Get all cart item components displayed on the page")
  public List<CartItemComponent> getAllCartItems() {
    log.info("Retrieving all cart item components");
    assertThat(cartListContainer.isVisible()).as("Cart list container must be visible").isTrue();

    if (cartItemContainers.count() == 0) {
      log.info("Cart is empty, returning empty list.");
      return Collections.emptyList();
    }

    return cartItemContainers.all().stream()
        .map(locator -> new CartItemComponent(this.page, locator))
        .toList();
  }

  /**
   * Finds and returns a CartItemComponent based on the product name.
   * Assumes product names are unique within the cart.
   * @param productName The exact name of the product to find.
   * @return CartItemComponent for the matching product.
   * @throws AssertionError if the product item with the given name is not found or not visible in the cart.
   */
  @Step("Get cart item component by name: {productName}")
  public CartItemComponent getCartItemByName(String productName) {
    log.info("Finding cart item component for name: '{}'", productName);
    assertThat(cartListContainer.isVisible()).as("Cart list must be visible to find item by name").isTrue();

    Locator itemLocator = cartItemContainers
        .filter(new Locator.FilterOptions().setHasText(productName))
        .first();

    assertThat(itemLocator.isVisible())
        .as("Cart item container for '%s' should be found and visible", productName)
        .isTrue();

    log.debug("Found locator for cart item '{}': {}", productName, itemLocator);
    return new CartItemComponent(this.page, itemLocator);
  }

  /**
   * Removes an item from the cart by its name.
   * @param productName The name of the product to remove.
   * @return CartPage (this) for chaining.
   */
  @Step("Removing item '{productName}' from cart")
  public CartPage removeItemByName(String productName) {
    log.info("Attempting to remove item '{}' from cart", productName);
    getCartItemByName(productName).remove();
    return this;
  }
}

