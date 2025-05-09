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
public class CheckoutStepOnePage extends BasePage {

  // Components
  private final HeaderComponent header;
  private final FooterComponent footer;

  // Component locators
  private final Locator headerLocator = page.locator("#header_container");
  private final Locator footerLocator = page.locator("footer.footer");

  // Page locators
  private final Locator pageTitle = page.locator(".title");
  private final Locator firstNameInput = page.locator("[data-test='firstName']");
  private final Locator lastNameInput = page.locator("[data-test='lastName']");
  private final Locator postalCodeInput = page.locator("[data-test='postalCode']");
  private final Locator cancelButton = page.locator("[data-test='cancel']");
  private final Locator continueButton = page.locator("[data-test='continue']");
  private final Locator errorMessageContainer = page.locator("[data-test='error']");

  /**
   * Constructor for CheckoutStepOnePage.
   * @param page The Playwright Page instance.
   */
  public CheckoutStepOnePage(Page page) {
    super(page);
    this.header = new HeaderComponent(this.page, this.headerLocator);
    this.footer = new FooterComponent(this.page, this.footerLocator);
    log.info("CheckoutStepOnePage object created for Page: {}", page.hashCode());
  }

  @Override
  @Step("Opening Checkout Step One Page directly (not typical)")
  public CheckoutStepOnePage open() {
    navigateTo("/checkout-step-one.html");
    return verifyLoaded();
  }

  @Override
  @Step("Verifying Checkout Step One Page essential elements are present")
  public CheckoutStepOnePage verifyLoaded() {
    log.info("Verifying Checkout Step One Page essential elements are present");
    assertThat(page.url()).as("Checkout Step One URL check").contains("/checkout-step-one.html");
    assertThat(pageTitle.isVisible()).as("Page title 'Checkout: Your Information' should be present").isTrue();
    assertThat(pageTitle.textContent()).as("Page title text check").isEqualToIgnoringCase("Checkout: Your Information");
    assertThat(firstNameInput.isVisible()).as("First Name input should be present").isTrue();
    assertThat(lastNameInput.isVisible()).as("Last Name input should be present").isTrue();
    assertThat(postalCodeInput.isVisible()).as("Postal Code input should be present").isTrue();
    assertThat(cancelButton.isVisible()).as("Cancel button should be present").isTrue();
    assertThat(continueButton.isVisible()).as("Continue button should be present").isTrue();
    assertThat(header.isVisible()).as("Header component should be present").isTrue();
    assertThat(footer.isVisible()).as("Footer component should be present").isTrue();
    return this;
  }

  @Step("Entering First Name: {firstName}")
  public CheckoutStepOnePage enterFirstName(String firstName) {
    log.info("Entering First Name: {}", firstName);
    firstNameInput.fill(firstName);
    return this;
  }

  @Step("Entering Last Name: {lastName}")
  public CheckoutStepOnePage enterLastName(String lastName) {
    log.info("Entering Last Name: {}", lastName);
    lastNameInput.fill(lastName);
    return this;
  }

  @Step("Entering Postal Code: {postalCode}")
  public CheckoutStepOnePage enterPostalCode(String postalCode) {
    log.info("Entering Postal Code: {}", postalCode);
    postalCodeInput.fill(postalCode);
    return this;
  }

  /**
   * Fills all information fields.
   * @param firstName First name
   * @param lastName Last name
   * @param postalCode Postal code
   * @return this page for chaining.
   */
  @Step("Filling checkout information: First Name='{firstName}', Last Name='{lastName}', Postal Code='{postalCode}'")
  public CheckoutStepOnePage fillInformation(String firstName, String lastName, String postalCode) {
    enterFirstName(firstName);
    enterLastName(lastName);
    enterPostalCode(postalCode);
    return this;
  }

  @Step("Clicking 'Continue' button")
  public CheckoutStepTwoPage clickContinue() {
    log.info("Clicking Continue button");
    continueButton.click();
    return new CheckoutStepTwoPage(this.page);
  }

  @Step("Clicking 'Cancel' button")
  public CartPage clickCancel() {
    log.info("Clicking Cancel button");
    cancelButton.click();
    return new CartPage(this.page);
  }

  @Step("Checking if error message is visible")
  public boolean isErrorVisible() {
    boolean visible = errorMessageContainer.isVisible();
    log.debug("Checkout Step One error message visibility: {}", visible);
    return visible;
  }

  @Step("Getting error message text")
  public String getErrorMessage() {
    assertThat(isErrorVisible()).as("Cannot get error message text - element not visible").isTrue();
    String errorText = errorMessageContainer.textContent();
    log.debug("Checkout Step One error message text: {}", errorText);
    return errorText;
  }
}