package com.nazarov.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.pages.base.BasePage;
import com.nazarov.saucedemo.pages.components.HeaderComponent;
import com.nazarov.saucedemo.pages.components.FooterComponent;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Getter
public class CheckoutCompletePage extends BasePage {

  // Components
  private final HeaderComponent header;
  private final FooterComponent footer;

  // Component locators
  private final Locator headerLocator = page.locator("#header_container");
  private final Locator footerLocator = page.locator("footer.footer");

  // Page locators
  private final Locator pageTitle = page.locator(".title");
  private final Locator completeHeader = page.locator(".complete-header");
  private final Locator completeText = page.locator(".complete-text");
  private final Locator ponyExpressImage = page.locator("img.pony_express");
  private final Locator backHomeButton = page.locator("[data-test='back-to-products']");

  /**
   * Constructor for CheckoutCompletePage.
   * @param page The Playwright Page instance.
   */
  public CheckoutCompletePage(Page page) {
    super(page);
    this.header = new HeaderComponent(this.page, this.headerLocator);
    this.footer = new FooterComponent(this.page, this.footerLocator);
    log.info("CheckoutCompletePage object created for Page: {}", page.hashCode());
  }

  @Override
  @Step("Opening Checkout Complete Page directly (not typical)")
  public CheckoutCompletePage open() {
    navigateTo("/checkout-complete.html");
    return verifyLoaded();
  }

  @Override
  @Step("Verifying Checkout Complete Page essential elements are present")
  public CheckoutCompletePage verifyLoaded() {
    log.info("Verifying Checkout Complete Page essential elements are present");
    assertThat(page.url()).as("Checkout Complete URL check").contains("/checkout-complete.html");
    assertThat(pageTitle.isVisible()).as("Page title 'Checkout: Complete!' should be present").isTrue();
    assertThat(pageTitle.textContent()).as("Page title text check").isEqualToIgnoringCase("Checkout: Complete!");
    assertThat(completeHeader.isVisible()).as("Completion header message should be present").isTrue();
    assertThat(completeText.isVisible()).as("Completion text message should be present").isTrue();
    assertThat(ponyExpressImage.isVisible()).as("Pony Express image should be present").isTrue();
    assertThat(backHomeButton.isVisible()).as("Back Home button should be present").isTrue();
    assertThat(header.isVisible()).as("Header component should be present").isTrue();
    assertThat(footer.isVisible()).as("Footer component should be present").isTrue();
    return this;
  }

  @Step("Get completion header text")
  public String getCompleteHeaderText() {
    String text = completeHeader.textContent();
    log.debug("Completion header text: {}", text);
    return text;
  }

  @Step("Get completion body text")
  public String getCompleteText() {
    String text = completeText.textContent();
    log.debug("Completion body text: {}", text);
    return text;
  }

  @Step("Clicking 'Back Home' button")
  public InventoryPage clickBackHome() {
    log.info("Clicking Back Home button");
    backHomeButton.click();
    return new InventoryPage(this.page);
  }
}