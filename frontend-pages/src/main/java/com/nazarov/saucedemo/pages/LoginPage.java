package com.nazarov.saucedemo.pages;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.pages.base.BasePage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginPage extends BasePage {

  // Page locators
  private final Locator usernameInput = page.locator("[data-test='username']");
  private final Locator passwordInput = page.locator("[data-test='password']");
  private final Locator loginButton = page.locator("[data-test='login-button']");
  private final Locator errorMessageContainer = page.locator("[data-test='error']");

  public LoginPage(Page page) {
    super(page);
    log.info("LoginPage object created");
  }

  @Override
  @Step("Opening Login Page")
  public LoginPage open() {
    navigateTo("");
    return verifyLoaded();
  }

  @Override
  @Step("Verifying Login Page essential elements are present")
  public LoginPage verifyLoaded() {
    log.info("Verifying Login Page essential elements are present");

    assertThat(page.url()).as("Login Page URL check").isEqualTo(appConfig.getBaseUri() + "/");
    assertThat(loginButton.isVisible()).as("Login button should be present").isTrue();
    assertThat(usernameInput.isVisible()).as("Username input should be present").isTrue();
    assertThat(passwordInput.isVisible()).as("Password input should be present").isTrue();
    return this;
  }

  @Step("Entering username: {username}")
  public LoginPage enterUsername(String username) {
    log.info("Entering username: {}", username);
    usernameInput.fill(username);
    return this;
  }

  @Step("Entering password: {password}")
  public LoginPage enterPassword(String password) {
    log.info("Entering password");
    passwordInput.fill(password);
    return this;
  }

  @Step("Clicking Login button")
  public void clickLoginButton() {
    log.info("Clicking Login button");
    loginButton.click();
  }

  /**
   * Performs login actions and returns the InventoryPage object *without* verifying it.
   * Verification should happen in the test.
   * @param username User login
   * @param password User password
   * @return InventoryPage object (potentially not fully loaded yet, test should verify)
   */
  @Step("Performing login actions as user: {username}")
  public InventoryPage performLogin(String username, String password) {
    enterUsername(username);
    enterPassword(password);
    clickLoginButton();
    return new InventoryPage(page);
  }

  /**
   * Attempts login by filling credentials and clicking login.
   * Does NOT assert the outcome (error or success).
   * @param username User login
   * @param password User password
   * @return LoginPage (returns self for potential chaining, but primarily performs action)
   */
  @Step("Attempting login with user: {username}")
  public LoginPage attemptLogin(String username, String password) {
    enterUsername(username);
    enterPassword(password);
    clickLoginButton();
    return this;
  }


  @Step("Checking if login error message is visible")
  public boolean isErrorVisible() {
    boolean visible = errorMessageContainer.isVisible();
    log.debug("Login error message visibility: {}", visible);
    return visible;
  }

  @Step("Getting login error message text")
  public String getErrorMessage() {
    assertThat(isErrorVisible()).as("Cannot get error message text - element not visible").isTrue();
    String errorText = errorMessageContainer.textContent();
    log.debug("Login error message text: {}", errorText);
    return errorText;
  }
}