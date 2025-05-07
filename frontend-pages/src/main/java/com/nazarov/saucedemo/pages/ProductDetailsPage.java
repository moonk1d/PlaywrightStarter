package com.nazarov.saucedemo.pages;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.pages.base.BasePage;
import com.nazarov.saucedemo.pages.components.FooterComponent;
import com.nazarov.saucedemo.pages.components.HeaderComponent;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ProductDetailsPage extends BasePage {

  // Components
  private final HeaderComponent header;
  private final FooterComponent footer;

  // Component locators
  private final Locator headerLocator = page.locator("#header_container");
  private final Locator footerLocator = page.locator("footer.footer");

  // Page locators
  private final Locator backToProductsButton = page.locator("[data-test='back-to-products']");
  private final Locator detailsContainer = page.locator(".inventory_details_container");
  private final Locator productImage = detailsContainer.locator("img.inventory_details_img");
  private final Locator productName = detailsContainer.locator(".inventory_details_name");
  private final Locator productDescription = detailsContainer.locator(".inventory_details_desc");
  private final Locator productPrice = detailsContainer.locator(".inventory_details_price");
  private final Locator addToCartButton = detailsContainer.locator(
      "button[data-test^='add-to-cart-']");
  private final Locator removeButton = detailsContainer.locator("button[data-test^='remove-']");

  /**
   * Constructor for ProductDetailsPage.
   *
   * @param page The Playwright Page instance.
   */
  public ProductDetailsPage(Page page) {
    super(page);
    this.header = new HeaderComponent(this.page, this.headerLocator);
    this.footer = new FooterComponent(this.page, this.footerLocator);
    log.info("ProductDetailsPage object created for Page: {}", page.hashCode());
  }

  @Override
  @Step("Attempting to open Product Details Page directly (not recommended)")
  public ProductDetailsPage open() {
    log.error(
        "ProductDetailsPage cannot be opened directly without a product ID. Please navigate from another page.");
    throw new UnsupportedOperationException(
        "ProductDetailsPage cannot be opened directly without a product ID.");
  }

  @Override
  @Step("Verifying Product Details Page essential elements are present")
  public ProductDetailsPage verifyLoaded() {
    log.info("Verifying Product Details Page essential elements are present");
    assertThat(page.url()).as("Product Details URL check").contains("/inventory-item.html?id=");
    assertThat(backToProductsButton.isVisible()).as("'Back to products' button should be present")
        .isTrue();
    assertThat(detailsContainer.isVisible()).as("Product details container should be present")
        .isTrue();
    assertThat(productImage.isVisible()).as("Product image should be present").isTrue();
    assertThat(productName.isVisible()).as("Product name should be present").isTrue();
    assertThat(productDescription.isVisible()).as("Product description should be present").isTrue();
    assertThat(productPrice.isVisible()).as("Product price should be present").isTrue();
    assertThat(addToCartButton.isVisible() || removeButton.isVisible())
        .as("Either 'Add to Cart' or 'Remove' button should be present")
        .isTrue();
    assertThat(header.isVisible()).as("Header component should be present").isTrue();
    assertThat(footer.isVisible()).as("Footer component should be present").isTrue();
    return this;
  }

  @Step("Clicking 'Back to products' button")
  public InventoryPage clickBackToProducts() {
    log.info("Clicking 'Back to products' button");
    backToProductsButton.click();
    return new InventoryPage(this.page);
  }

  @Step("Get product name from details page")
  public String getName() {
    String name = productName.textContent();
    log.debug("Product name on details page: {}", name);
    return name;
  }

  @Step("Get product description from details page")
  public String getDescription() {
    String desc = productDescription.textContent();
    log.debug("Product description on details page: {}", desc);
    return desc;
  }

  @Step("Get product price from details page")
  public String getPrice() {
    String price = productPrice.textContent();
    log.debug("Product price on details page: {}", price);
    return price;
  }

  @Step("Get product image source URL from details page")
  public String getImageUrl() {
    String url = productImage.getAttribute("src");
    log.debug("Product image URL on details page: {}", url);
    return url;
  }

  @Step("Click 'Add to Cart' button on details page")
  public ProductDetailsPage addToCart() {
    log.info("Clicking 'Add to Cart' on details page for product: {}", getName());
    assertThat(isAddToCartButtonVisible()).as(
        "Add to Cart button should be visible before clicking").isTrue();
    addToCartButton.click();
    return this;
  }

  @Step("Click 'Remove' button on details page")
  public ProductDetailsPage removeFromCart() {
    String currentProductName = getName();
    log.info("Clicking 'Remove' on details page for product: {}", currentProductName);
    assertThat(isRemoveButtonVisible()).as("Remove button should be visible before clicking")
        .isTrue();
    removeButton.click();
    return this;
  }

  @Step("Check if 'Add to Cart' button is visible on details page")
  public boolean isAddToCartButtonVisible() {
    boolean isVisible = addToCartButton.isVisible();
    log.trace("Is 'Add to Cart' visible on details page: {}", isVisible);
    return isVisible;
  }

  @Step("Check if 'Remove' button is visible on details page")
  public boolean isRemoveButtonVisible() {
    boolean isVisible = removeButton.isVisible();
    log.trace("Is 'Remove' visible on details page: {}", isVisible);
    return isVisible;
  }
}
