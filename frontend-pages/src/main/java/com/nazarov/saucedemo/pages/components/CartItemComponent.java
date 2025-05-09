package com.nazarov.saucedemo.pages.components;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.pages.ProductDetailsPage;
import com.nazarov.saucedemo.pages.base.BaseComponent;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a single item row in the shopping cart list.
 */
@Slf4j
public class CartItemComponent extends BaseComponent {

  // Locators relative to the item's root container (e.g., ".cart_item")
  private final Locator quantity = locator(".cart_quantity");
  private final Locator nameLink = locator(".inventory_item_name");
  private final Locator description = locator(".inventory_item_desc");
  private final Locator price = locator(".inventory_item_price");
  private final Locator removeButton = locator("button[data-test^='remove-']");

  /**
   * Constructor for CartItemComponent.
   *
   * @param page        The Playwright Page object.
   * @param rootLocator The Locator pointing to the root element of this specific cart item row.
   */
  public CartItemComponent(Page page, Locator rootLocator) {
    super(page, rootLocator);
     log.debug("CartItemComponent created for locator: {}", rootLocator);
  }

  @Step("Get item quantity")
  public int getQuantity() {
    String quantityText = quantity.textContent();
    log.trace("Item quantity text for component {}: {}", rootLocator, quantityText);
    try {
      return Integer.parseInt(quantityText);
    } catch (NumberFormatException e) {
      log.error("Could not parse quantity text '{}' to integer for cart item {}", quantityText,
          rootLocator, e);
      throw new IllegalStateException(
          "Cart item quantity text '" + quantityText + "' is not a valid integer.");
    }
  }

  @Step("Get item name")
  public String getName() {
    String itemName = nameLink.textContent();
    log.trace("Item name for component {}: {}", rootLocator, itemName);
    return itemName;
  }

  @Step("Get item description")
  public String getDescription() {
    String itemDesc = description.textContent();
    log.trace("Item description for component {}: {}", rootLocator, itemDesc);
    return itemDesc;
  }

  @Step("Get item price")
  public String getPrice() {
    // Returns the price string including the '$' sign
    String itemPrice = price.textContent();
    log.trace("Item price for component {}: {}", rootLocator, itemPrice);
    return itemPrice;
  }

  @Step("Click 'Remove' button for item")
  public void remove() {
    String itemName = getName(); // Get name before potential DOM change
    log.debug("Clicking 'Remove' for cart item: {}", itemName);
    assertThat(removeButton.isVisible()).as("Remove button should be visible for cart item '%s'",
        itemName).isTrue();
    removeButton.click();
    // The item is removed from the DOM. The test should verify the cart state afterwards.
    // Consider adding a wait for the element to be detached if needed, although often not required.
    // rootLocator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
  }

  @Step("Click item name link to view details")
  public ProductDetailsPage clickNameLink() {
    String itemName = getName();
    log.info("Clicking name link for cart item: {}", itemName);
    nameLink.click();
    return new ProductDetailsPage(this.page);
  }
}