package com.nazarov.saucedemo.functional;

import static org.assertj.core.api.Assertions.assertThat;

import com.nazarov.saucedemo.TestFixture;
import com.nazarov.saucedemo.config.AppConfig;
import com.nazarov.saucedemo.pages.InventoryPage;
import com.nazarov.saucedemo.pages.LoginPage;
import com.nazarov.saucedemo.pages.components.MenuComponent;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Epic("SauceDemo Application")
@Feature("Authentication")
@Tag("functional")
@Tag("smoke")
@Tag("regression")
class AuthenticationTests extends TestFixture {

  @Autowired
  private AppConfig appConfig;

  @Test
  @Severity(SeverityLevel.CRITICAL)
  @Story("Successful Login")
  @DisplayName("Verify successful login with standard user")
  @Description("User should be able to log in with valid standard_user credentials and land on the inventory page.")
  void successfulLoginTest() {
    LoginPage loginPage = new LoginPage(page).open();

    InventoryPage inventoryPage = loginPage.performLogin("standard_user", "secret_sauce");
    inventoryPage.verifyLoaded();

    assertThat(inventoryPage.getCurrentUrl())
        .as("URL should be the inventory page")
        .isEqualTo(appConfig.getBaseUri() + "/inventory.html");
    assertThat(inventoryPage.getHeader().isVisible())
        .as("Header component should be visible after login")
        .isTrue();
    assertThat(inventoryPage.getProductItemByName("Sauce Labs Backpack")
        .isVisible())
        .as("A known product item should be visible on the inventory page")
        .isTrue();
  }

  @Test
  @Severity(SeverityLevel.CRITICAL)
  @Story("Successful Logout")
  @DisplayName("Verify successful logout from inventory page")
  @Description("User should be able to log out from the application via the burger menu.")
  void successfulLogoutTest() {
    LoginPage loginPage = new LoginPage(page).open();
    InventoryPage inventoryPage = loginPage.performLogin("standard_user", "secret_sauce");
    inventoryPage.verifyLoaded();

    // Open menu and logout
    MenuComponent menu = inventoryPage.getHeader().openBurgerMenu();
    LoginPage loginPageAfterLogout = menu.clickLogout();

    loginPageAfterLogout.verifyLoaded();
    assertThat(loginPageAfterLogout.getCurrentUrl())
        .as("URL should be the login page after logout")
        .isEqualTo(appConfig.getBaseUri() + "/");
    assertThat(loginPageAfterLogout.isErrorVisible())
        .as("No error message should be visible on the login page after logout")
        .isFalse();
  }

  @Test
  @Severity(SeverityLevel.NORMAL)
  @Story("Failed Login")
  @DisplayName("Verify login fails with locked out user")
  @Description("User should see an error message when attempting to log in with locked_out_user credentials.")
  void lockedOutUserLoginTest() {
    LoginPage loginPage = new LoginPage(page).open();

    loginPage.attemptLogin("locked_out_user", "secret_sauce");

    assertThat(loginPage.isErrorVisible())
        .as("Error message should be visible for locked out user")
        .isTrue();
    assertThat(loginPage.getErrorMessage())
        .as("Error message content check")
        .contains("Sorry, this user has been locked out.");
    assertThat(loginPage.getCurrentUrl())
        .as("Should remain on the login page after failed login")
        .isEqualTo(appConfig.getBaseUri() + "/");
  }
}