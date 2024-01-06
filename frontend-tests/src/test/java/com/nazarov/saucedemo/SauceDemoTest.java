package com.nazarov.saucedemo;

import com.nazarov.saucedemo.pages.LoginPage;
import com.nazarov.saucedemo.pages.ProductsPage;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class SauceDemoTest extends TestFixture {

  @Test
  void expectedTestPass() {
    // pass
    log.info("This test should pass");
    LoginPage loginPage = new LoginPage(pw.getPage()).open();
    loginPage.getUsernameInput().fill("standard_user");
    loginPage.getPasswordInput().fill("secret_sauce");
    loginPage.getLoginButton().click();

    ProductsPage productsPage = new ProductsPage(pw.getPage());
    Assertions.assertThat(productsPage.getTitle().innerText()).isEqualTo("Products");
  }

  @Test
  void expectedTestFailOnError() {
    // fail
    LoginPage loginPage = new LoginPage(pw.getPage()).open();
    loginPage.getUsernameInput().fill("standard_user1");
    loginPage.getPasswordInput().fill("secret_sauce");
    loginPage.getLoginButton().click();

    ProductsPage productsPage = new ProductsPage(pw.getPage());
    Assertions.assertThat(productsPage.getTitle().innerText()).isEqualTo("Products");
  }

  @Test
  void expectedTestFailOnAssertion() {
    // pass
    LoginPage loginPage = new LoginPage(pw.getPage()).open();
    loginPage.login("standard_user", "secret_sauce");
    ProductsPage productsPage = new ProductsPage(pw.getPage());
    Assertions.assertThat(productsPage.getTitle().innerText()).isEqualTo("Cart");
  }

}
