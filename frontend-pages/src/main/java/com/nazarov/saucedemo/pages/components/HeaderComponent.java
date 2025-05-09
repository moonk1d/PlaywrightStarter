package com.nazarov.saucedemo.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.nazarov.saucedemo.pages.CartPage;
import com.nazarov.saucedemo.pages.base.BaseComponent;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeaderComponent extends BaseComponent {

  private final Locator burgerMenuButton = locator("#react-burger-menu-btn");
  private final Locator shoppingCartLink = locator(".shopping_cart_link");
  private final Locator shoppingCartBadge = shoppingCartLink.locator(".shopping_cart_badge");

  public HeaderComponent(Page page, Locator rootLocator) {
    super(page, rootLocator);
    log.info("HeaderComponent object created for locator: {}", rootLocator);
  }

  @Step("Opening Burger Menu")
  public MenuComponent openBurgerMenu() {
    log.info("Clicking burger menu button");
    burgerMenuButton.click();
    Locator menuRootLocator = page.locator(".bm-menu-wrap");

    log.debug("Waiting for menu root locator to become visible: {}", menuRootLocator);
    menuRootLocator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    log.info("Burger menu opened, returning MenuComponent instance");

    return new MenuComponent(this.page, menuRootLocator);
  }

  @Step("Getting Cart Item Count from Badge")
  public int getCartItemCount() {
    if (shoppingCartBadge.isVisible()) {
      String countText = shoppingCartBadge.textContent();
      log.debug("Cart badge text: {}", countText);
      try {
        return Integer.parseInt(countText);
      } catch (NumberFormatException e) {
        log.error("Could not parse cart badge text '{}' to integer", countText, e);
        // Decide how to handle parse errors - throw exception or return default?
        // Throwing might be better to fail fast if the badge text is unexpected.
        throw new IllegalStateException(
            "Cart badge text '" + countText + "' is not a valid integer.");
      }
    } else {
      log.debug("Cart badge not visible, assuming 0 items");
      return 0;
    }
  }

  @Step("Clicking Shopping Cart Icon")
  public CartPage goToCart() {
    log.info("Clicking shopping cart link");
    shoppingCartLink.click();
    return new CartPage(page);
  }
}