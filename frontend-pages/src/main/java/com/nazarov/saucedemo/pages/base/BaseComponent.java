package com.nazarov.saucedemo.pages.base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class BaseComponent {

  protected final Page page;
  protected final Locator rootLocator;

  /**
   * Constructor for BaseComponent.
   *
   * @param page        The Playwright Page object.
   * @param rootLocator The Locator pointing to the root element of this component.
   */
  protected BaseComponent(Page page, Locator rootLocator) {
    this.page = page;
    this.rootLocator = rootLocator;
    log.debug("BaseComponent initialized for locator: {}", rootLocator);
  }

  /**
   * Finds a locator relative to the component's root locator.
   *
   * @param selector CSS or XPath selector relative to the root.
   * @return Locator instance.
   */
  protected Locator locator(String selector) {
    return this.rootLocator.locator(selector);
  }

  /**
   * Finds a locator relative to the component's root locator using test ID. Requires
   * testIdAttribute to be configured.
   *
   * @param testId The value of the data-testid attribute.
   * @return Locator instance.
   */
  protected Locator locatorByTestId(String testId) {
    return this.rootLocator.getByTestId(testId);
  }

  /**
   * Checks if the root element of the component is visible. Uses a short timeout for the check.
   *
   * @return true if visible, false otherwise.
   */
  public boolean isVisible() {
    try {
      rootLocator.waitFor(
          new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(1000));
      boolean visible = rootLocator.isVisible();
      log.trace("Component root locator visibility [{}]: {}", rootLocator, visible);
      return visible;
    } catch (TimeoutError e) {
      log.trace("Component root locator not visible within timeout: {}", rootLocator);
      return false;
    }
  }

}