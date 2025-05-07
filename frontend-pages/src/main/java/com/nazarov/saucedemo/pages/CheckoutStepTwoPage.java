package com.nazarov.saucedemo.pages;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.pages.base.BasePage;
import com.nazarov.saucedemo.pages.components.CartItemComponent;
import com.nazarov.saucedemo.pages.components.FooterComponent;
import com.nazarov.saucedemo.pages.components.HeaderComponent;
import io.qameta.allure.Step;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class CheckoutStepTwoPage extends BasePage {

  // Components
  private final HeaderComponent header;
  private final FooterComponent footer;

  // Component locators
  private final Locator headerLocator = page.locator("#header_container");
  private final Locator footerLocator = page.locator("footer.footer");

  // Page locators
  private final Locator pageTitle = page.locator(".title");
  private final Locator cartListContainer = page.locator(".cart_list");
  private final Locator overviewItemContainers = cartListContainer.locator(
      ".cart_item");
  private final Locator summaryInfoContainer = page.locator(".summary_info");
  private final Locator paymentInformationLabel = summaryInfoContainer.getByTestId(
      "payment-info-label").filter(new Locator.FilterOptions().setHasText("Payment Information"));
  private final Locator shippingInformationLabel = summaryInfoContainer.getByTestId(
      "shipping-info-label").filter(new Locator.FilterOptions().setHasText("Shipping Information"));
  private final Locator itemTotalLabel = summaryInfoContainer.locator(".summary_subtotal_label");
  private final Locator taxLabel = summaryInfoContainer.locator(".summary_tax_label");
  private final Locator totalLabel = summaryInfoContainer.locator(
      ".summary_total_label");
  private final Locator cancelButton = page.locator("[data-test='cancel']");
  private final Locator finishButton = page.locator("[data-test='finish']");

  /**
   * Constructor for CheckoutStepTwoPage.
   *
   * @param page The Playwright Page instance.
   */
  public CheckoutStepTwoPage(Page page) {
    super(page);
    this.header = new HeaderComponent(this.page, this.headerLocator);
    this.footer = new FooterComponent(this.page, this.footerLocator);
    log.info("CheckoutStepTwoPage object created for Page: {}", page.hashCode());
  }

  @Override
  @Step("Opening Checkout Step Two Page directly (not typical)")
  public CheckoutStepTwoPage open() {
    navigateTo("/checkout-step-two.html");
    return verifyLoaded();
  }

  @Override
  @Step("Verifying Checkout Step Two Page essential elements are present")
  public CheckoutStepTwoPage verifyLoaded() {
    log.info("Verifying Checkout Step Two Page essential elements are present");
    assertThat(page.url()).as("Checkout Step Two URL check").contains("/checkout-step-two.html");
    assertThat(pageTitle.isVisible()).as("Page title 'Checkout: Overview' should be present")
        .isTrue();
    assertThat(pageTitle.textContent()).as("Page title text check")
        .isEqualToIgnoringCase("Checkout: Overview");
    assertThat(cartListContainer.isVisible()).as("Cart list container should be present").isTrue();
    assertThat(summaryInfoContainer.isVisible()).as("Summary info container should be present")
        .isTrue();
    assertThat(paymentInformationLabel.isVisible()).as(
        "Payment Information label should be present").isTrue();
    assertThat(shippingInformationLabel.isVisible()).as(
        "Shipping Information label should be present").isTrue();
    assertThat(itemTotalLabel.isVisible()).as("Item total label should be present").isTrue();
    assertThat(taxLabel.isVisible()).as("Tax label should be present").isTrue();
    assertThat(totalLabel.isVisible()).as("Total label should be present").isTrue();
    assertThat(cancelButton.isVisible()).as("Cancel button should be present").isTrue();
    assertThat(finishButton.isVisible()).as("Finish button should be present").isTrue();
    assertThat(header.isVisible()).as("Header component should be present").isTrue();
    assertThat(footer.isVisible()).as("Footer component should be present").isTrue();
    return this;
  }

  /**
   * Retrieves a list of CartItemComponent instances for all items in the overview.
   *
   * @return List of CartItemComponent.
   */
  @Step("Get all overview item components")
  public List<CartItemComponent> getAllOverviewItems() {
    log.info("Retrieving all overview item components");
    assertThat(cartListContainer.isVisible()).as("Cart list must be visible to get items").isTrue();
    if (overviewItemContainers.count() == 0) {
      log.info("Overview list is empty.");
      return Collections.emptyList();
    }
    return overviewItemContainers.all().stream()
        .map(locator -> new CartItemComponent(this.page, locator))
        .toList();
  }

  @Step("Get Item Total price text")
  public String getItemTotalText() {
    String text = itemTotalLabel.textContent();
    log.debug("Item total text: {}", text);
    return text; // e.g., "Item total: $29.99"
  }

  @Step("Get Tax text")
  public String getTaxText() {
    String text = taxLabel.textContent();
    log.debug("Tax text: {}", text);
    return text; // e.g., "Tax: $2.40"
  }

  @Step("Get Total price text")
  public String getTotalPriceText() {
    String text = totalLabel.textContent();
    log.debug("Total price text: {}", text);
    return text; // e.g., "Total: $32.39"
  }

  @Step("Clicking 'Finish' button")
  public CheckoutCompletePage clickFinish() {
    log.info("Clicking Finish button");
    finishButton.click();
    return new CheckoutCompletePage(this.page);
  }

  @Step("Clicking 'Cancel' button")
  public InventoryPage clickCancel() {
    log.info("Clicking Cancel button");
    cancelButton.click();
    return new InventoryPage(this.page);
  }
}