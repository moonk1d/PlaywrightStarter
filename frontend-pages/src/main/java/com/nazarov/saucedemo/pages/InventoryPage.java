package com.nazarov.saucedemo.pages;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.pages.base.BasePage;
import com.nazarov.saucedemo.pages.components.FooterComponent;
import com.nazarov.saucedemo.pages.components.HeaderComponent;
import com.nazarov.saucedemo.pages.components.ProductItemComponent;
import io.qameta.allure.Step;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class InventoryPage extends BasePage {

  // Components
  private final HeaderComponent header;
  private final FooterComponent footer;

  // Component locators
  private final Locator headerLocator = page.locator("#header_container");
  private final Locator footerLocator = page.locator("footer.footer");

  // Page locators
  private final Locator pageTitle = page.locator(".title");
  private final Locator inventoryList = page.locator(".inventory_list");
  private final Locator inventoryItems = inventoryList.locator(".inventory_item");

  public InventoryPage(Page page) {
    super(page);
    this.header = new HeaderComponent(page, headerLocator);
    this.footer = new FooterComponent(page, footerLocator);
    log.info("InventoryPage object created");
  }

  @Override
  public InventoryPage open() {
    log.warn(
        "InventoryPage is not intended to be opened directly. Assuming user is already logged in.");
    return verifyLoaded();
  }

  @Override
  @Step("Verifying Inventory Page essential elements are present")
  public InventoryPage verifyLoaded() {
    log.info("Verifying Inventory Page essential elements are present");

    assertThat(page.url()).as("Inventory Page URL check").contains("/inventory.html");
    assertThat(pageTitle.isVisible()).as("Page title 'Products' should be present").isTrue();
    assertThat(pageTitle.textContent()).as("Page title text check")
        .isEqualToIgnoringCase("Products");
    assertThat(inventoryList.isVisible()).as("Inventory list container should be present").isTrue();
    assertThat(header.isVisible()).as("Header component should be present").isTrue();
    return this;
  }

  /**
   * Retrieves a list of ProductItemComponent instances for all items currently displayed.
   *
   * @return List of ProductItemComponent.
   */
  @Step("Get all product item components displayed on the page")
  public List<ProductItemComponent> getAllProductItems() {
    log.info("Retrieving all product item components");
    assertThat(inventoryList.isVisible()).as("Inventory list must be visible to get items")
        .isTrue();

    return inventoryItems.all().stream()
        .map(locator -> new ProductItemComponent(this.page, locator))
        .toList();
  }

  /**
   * Finds and returns a ProductItemComponent based on the product name. Assumes product names are
   * unique as displayed on the page.
   *
   * @param productName The exact name of the product to find.
   * @return ProductItemComponent for the matching product.
   * @throws AssertionError if the product item with the given name is not found or not visible.
   */
  @Step("Get product item component by name: {productName}")
  public ProductItemComponent getProductItemByName(String productName) {
    log.info("Finding product item component for name: '{}'", productName);
    assertThat(inventoryList.isVisible()).as("Inventory list must be visible to find item by name")
        .isTrue();

    Locator itemLocator = inventoryItems
        .filter(new Locator.FilterOptions().setHasText(productName))
        .first();

    assertThat(itemLocator.isVisible())
        .as("Product item container for '%s' should be found and visible", productName)
        .isTrue();

    log.debug("Found locator for product '{}': {}", productName, itemLocator);
    return new ProductItemComponent(this.page, itemLocator);
  }

}