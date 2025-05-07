package com.nazarov.saucedemo.pages.components;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.pages.base.BaseComponent;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FooterComponent extends BaseComponent {

  // Locators relative to the footer's root container (e.g., "footer.footer")
  private final Locator twitterLink = locator("a[href*='twitter.com/saucelabs']");
  private final Locator facebookLink = locator("a[href*='facebook.com/saucelabs']");
  private final Locator linkedinLink = locator("a[href*='linkedin.com/company/saucelabs']");
  private final Locator copyrightText = locator(".footer_copy");

  /**
   * Constructor for FooterComponent.
   *
   * @param page        The Playwright Page object.
   * @param rootLocator The Locator pointing to the root element of the footer.
   */
  public FooterComponent(Page page, Locator rootLocator) {
    super(page, rootLocator);
    log.info("FooterComponent object created for locator: {}", rootLocator);
    assertThat(isVisible()).as("Footer component root should be visible on instantiation").isTrue();
  }

  @Step("Click Twitter link in footer")
  public void clickTwitterLink() {
    log.info("Clicking Twitter link in footer");
    twitterLink.click();
    // Test should handle verification of new tab/page if needed
  }

  @Step("Click Facebook link in footer")
  public void clickFacebookLink() {
    log.info("Clicking Facebook link in footer");
    facebookLink.click();
    // Test should handle verification
  }

  @Step("Click LinkedIn link in footer")
  public void clickLinkedInLink() {
    log.info("Clicking LinkedIn link in footer");
    linkedinLink.click();
    // Test should handle verification
  }

  @Step("Get copyright text from footer")
  public String getCopyrightText() {
    String text = copyrightText.textContent();
    log.debug("Footer copyright text: {}", text);
    return text;
  }
}