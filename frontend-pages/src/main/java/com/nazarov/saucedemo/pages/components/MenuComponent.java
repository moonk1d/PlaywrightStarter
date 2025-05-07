package com.nazarov.saucedemo.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.nazarov.saucedemo.pages.InventoryPage;
import com.nazarov.saucedemo.pages.LoginPage;
import com.nazarov.saucedemo.pages.base.BaseComponent;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Represents the slide-out burger menu component.
 * Assumes it becomes visible after clicking the burger icon in the Header.
 */
@Slf4j
public class MenuComponent extends BaseComponent {

  // Locators relative to the menu's root container (e.g., ".bm-menu-wrap")
  private final Locator inventoryLink = locator("#inventory_sidebar_link");
  private final Locator aboutLink = locator("#about_sidebar_link");
  private final Locator logoutLink = locator("#logout_sidebar_link");
  private final Locator resetAppStateLink = locator("#reset_sidebar_link");
  private final Locator closeButton = locator("#react-burger-cross-btn");

  /**
   * Constructor for MenuComponent.
   * @param page The Playwright Page object.
   * @param rootLocator The Locator pointing to the root element of the menu container (e.g., ".bm-menu-wrap").
   */
  public MenuComponent(Page page, Locator rootLocator) {
    super(page, rootLocator);
    log.info("MenuComponent object created for locator: {}", rootLocator);
    // Optional: Verify the component is visible upon creation
    assertThat(isVisible()).as("Menu component root should be visible on instantiation").isTrue();
  }

  @Step("Clicking 'All Items' link in menu")
  public InventoryPage clickInventory() {
    log.info("Clicking Inventory link in menu");
    inventoryLink.click();
    // Assuming navigation to InventoryPage
    return new InventoryPage(this.page); // Test should verify this page loaded
  }

  @Step("Clicking 'About' link in menu")
  public void clickAbout() {
    // Note: This navigates to saucelabs.com, potentially outside the test scope
    log.info("Clicking About link in menu");
    aboutLink.click();
    // Test should handle verification if navigation to external site is expected
  }

  @Step("Clicking 'Logout' link in menu")
  public LoginPage clickLogout() {
    log.info("Clicking Logout link in menu");
    logoutLink.click();
    // Assuming navigation to LoginPage
    return new LoginPage(this.page); // Test should verify this page loaded
  }

  @Step("Clicking 'Reset App State' link in menu")
  public MenuComponent clickResetAppState() {
    log.info("Clicking Reset App State link in menu");
    resetAppStateLink.click();
    // Usually stays on the same page, menu likely remains open
    // Test should verify the effects of resetting state (e.g., cart empty)
    return this;
  }

  @Step("Closing menu using the 'X' button")
  public void closeMenu() {
    log.info("Clicking close menu button ('X')");
    closeButton.click();
    // Add a wait to ensure the menu is hidden after clicking close
    log.debug("Waiting for menu root locator to become hidden: {}", rootLocator);
    rootLocator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    log.info("Menu closed");
  }
}