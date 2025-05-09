package com.nazarov.saucedemo.pages.components;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.pages.ProductDetailsPage;
import com.nazarov.saucedemo.pages.base.BaseComponent;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a single product item component displayed in lists (e.g., Inventory, Cart).
 */
@Slf4j
public class ProductItemComponent extends BaseComponent {

  private final Locator nameLink = locator(".inventory_item_name");
  private final Locator description = locator(".inventory_item_desc");
  private final Locator price = locator(".inventory_item_price");
  private final Locator image = locator(".inventory_item_img img");
  private final Locator addToCartButton = locator("button[data-test^='add-to-cart-']");
  private final Locator removeButton = locator("button[data-test^='remove-']");

  /**
   * Constructor for ProductItemComponent.
   *
   * @param page        The Playwright Page object.
   * @param rootLocator The Locator pointing to the root element of this specific product item.
   */
  public ProductItemComponent(Page page, Locator rootLocator) {
    super(page, rootLocator);
    log.debug("ProductItemComponent created for locator: {}", rootLocator);
  }

  @Step("Get product name")
  public String getName() {
    String productName = nameLink.textContent();
    log.trace("Product name for component {}: {}", rootLocator, productName);
    return productName;
  }

  @Step("Get product description")
  public String getDescription() {
    String productDesc = description.textContent();
    log.trace("Product description for component {}: {}", rootLocator, productDesc);
    return productDesc;
  }

  @Step("Get product price")
  public String getPrice() {
    // Returns the price string including the '$' sign
    String productPrice = price.textContent();
    log.trace("Product price for component {}: {}", rootLocator, productPrice);
    return productPrice;
  }

  @Step("Get product image source URL")
  public String getImageUrl() {
    String imageUrl = image.getAttribute("src");
    log.trace("Product image URL for component {}: {}", rootLocator, imageUrl);
    return imageUrl;
  }

  @Step("Click 'Add to Cart' button")
  public ProductItemComponent addToCart() {
    log.debug("Clicking 'Add to Cart' for product: {}", getName());
    assertThat(isAddToCartButtonVisible()).as(
        "Add to Cart button should be visible before clicking").isTrue();
    addToCartButton.click();
    return this;
  }

  @Step("Click 'Remove' button")
  public ProductItemComponent removeFromCart() {
    String productName = getName();
    log.debug("Clicking 'Remove' for product: {}", productName);
    assertThat(isRemoveButtonVisible()).as("Remove button should be visible before clicking")
        .isTrue();
    removeButton.click();
    return this;
  }

  @Step("Check if 'Add to Cart' button is visible")
  public boolean isAddToCartButtonVisible() {
    boolean isVisible = addToCartButton.isVisible();
    log.trace("Is 'Add to Cart' visible for {}: {}", getName(), isVisible);
    return isVisible;
  }

  @Step("Check if 'Remove' button is visible")
  public boolean isRemoveButtonVisible() {
    boolean isVisible = removeButton.isVisible();
    log.trace("Is 'Remove' visible for {}: {}", getName(), isVisible);
    return isVisible;
  }

  @Step("Click product name link to view details")
  public ProductDetailsPage clickNameLink() {
    String productName = getName();
    log.info("Clicking name link for product: {}", productName);
    nameLink.click();
    return new ProductDetailsPage(this.page);
  }
}