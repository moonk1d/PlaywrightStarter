package com.nazarov.saucedemo;

import com.microsoft.playwright.Locator;
import com.nazarov.saucedemo.pages.LoginPage;
import com.nazarov.saucedemo.pages.ProductsPage;
import com.nazarov.saucedemo.pages.components.InventoryItem;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class SauceDemoTest extends TestFixture {

  @Test
  void expectedTestPass() {
    // pass
    log.info("This test should pass");
    LoginPage loginPage = new LoginPage(page).open();
    loginPage.getUsernameInput().fill("standard_user");
    loginPage.getPasswordInput().fill("secret_sauce");
    loginPage.getLoginButton().click();

    ProductsPage productsPage = new ProductsPage(page);
    Assertions.assertThat(productsPage.getTitle().innerText()).isEqualTo("Products");
  }

  @Test
  void expectedTestFailOnError() {
    // fail
    log.info("This test should fail on exception");
    LoginPage loginPage = new LoginPage(page).open();
    loginPage.getUsernameInput().fill("standard_user1");
    loginPage.getPasswordInput().fill("secret_sauce");
    loginPage.getLoginButton().click();

    ProductsPage productsPage = new ProductsPage(page);
    Assertions.assertThat(productsPage.getTitle().innerText()).isEqualTo("Products");
  }

  @Test
  void expectedTestFailOnAssertion() {
    // pass
    log.info("This test should fail on assertion");
    LoginPage loginPage = new LoginPage(page).open();
    loginPage.login("standard_user", "secret_sauce");
    ProductsPage productsPage = new ProductsPage(page);
    Assertions.assertThat(productsPage.getTitle().innerText()).isEqualTo("Cart");
  }

  @Test
  void pageComponent() {
    // pass
    LoginPage loginPage = new LoginPage(page).open();
    loginPage.login("standard_user", "secret_sauce");

    ProductsPage productsPage = new ProductsPage(page);
    InventoryItem inventoryItem = productsPage.getInventoryItems().get(0);
    Locator button = inventoryItem.getAddCartButton();
    button.click();

    Assertions.assertThat(button.textContent()).isEqualTo("Remove");
  }
}
