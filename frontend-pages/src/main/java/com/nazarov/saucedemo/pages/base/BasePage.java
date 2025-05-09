package com.nazarov.saucedemo.pages.base;

import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.config.AppConfig;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BasePage {

  protected final Page page;
  protected final AppConfig appConfig;

  /**
   * Constructor initializes the Page object using PlaywrightManager and gets the AppConfig
   * instance.
   */
  protected BasePage(Page page) {
    this.page = page;
    this.appConfig = AppConfig.get();
    log.debug("BasePage initialized for class: {}", this.getClass().getSimpleName());
  }

  /**
   * Abstract method to force subclasses to implement their specific way of opening/navigating to
   * the page.
   *
   * @return The specific page instance (for fluent API).
   */
  @Step("Opening {this.getClass().getSimpleName()}")
  public abstract <T extends BasePage> T open();

  /**
   * Abstract method to force subclasses to implement a check to verify the page is loaded
   * correctly.
   *
   * @return The specific page instance (for fluent API).
   */
  @Step("Verifying {this.getClass().getSimpleName()} is loaded")
  public abstract <T extends BasePage> T verifyLoaded();


  /**
   * Navigates to a specific path relative to the base URI.
   *
   * @param path The relative path (e.g., "/inventory.html").
   */
  protected void navigateTo(String path) {
    String url = appConfig.getBaseUri() + path;
    Allure.step("Navigate to: " + url, step -> {
      step.parameter("URL: ", url);
      log.info("Navigating to path: {}", url);
      page.navigate(url);
    });
  }

  /**
   * Gets the current page URL.
   *
   * @return Page URL string.
   */
  public String getCurrentUrl() {
    String url = page.url();
    log.debug("Current page URL: {}", url);
    return url;
  }

}