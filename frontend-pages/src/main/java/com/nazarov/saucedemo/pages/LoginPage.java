package com.nazarov.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
public class LoginPage extends SauceDemoPage {

  private Locator usernameInput;
  private Locator passwordInput;
  private Locator loginButton;

  public LoginPage(Page page) {
    super(page);
    usernameInput = page.getByTestId("username");
    passwordInput = page.getByTestId("password");
    loginButton = page.getByTestId("login-button");
  }

  @Override
  public LoginPage open() {
    log.info("Open {} page", this.getClass().getName());
    page.navigate("https://www.saucedemo.com/");
    return this;
  }

  public void login(String username, String password) {
    usernameInput.fill(username);
    passwordInput.fill(password);
    loginButton.click();
  }
}
