package com.nazarov.saucedemo.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import com.nazarov.saucedemo.TestFixture;
import com.nazarov.saucedemo.config.AppConfig;
import com.nazarov.saucedemo.pages.CartPage;
import com.nazarov.saucedemo.pages.CheckoutCompletePage;
import com.nazarov.saucedemo.pages.CheckoutStepOnePage;
import com.nazarov.saucedemo.pages.CheckoutStepTwoPage;
import com.nazarov.saucedemo.pages.InventoryPage;
import com.nazarov.saucedemo.pages.LoginPage;
import com.nazarov.saucedemo.pages.components.CartItemComponent;
import com.nazarov.saucedemo.pages.components.ProductItemComponent;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Epic("SauceDemo Application")
@Feature("E2E Purchase Flow")
@Tag("e2e")
@Tag("smoke")
@Tag("regression")
class PurchaseFlowTests extends TestFixture {

  @Autowired
  private AppConfig appConfig;

  private final String PRODUCT_1_NAME = "Sauce Labs Backpack";
  private final String PRODUCT_1_PRICE = "$29.99";
  private final String PRODUCT_2_NAME = "Sauce Labs Bike Light";

  private final String FIRST_NAME = "John";
  private final String LAST_NAME = "Doe";
  private final String POSTAL_CODE = "12345";

  @Test
  @Severity(SeverityLevel.CRITICAL)
  @Story("Single Item Purchase")
  @DisplayName("Verify successful end-to-end purchase of a single item")
  @Description("User logs in, adds an item to cart, proceeds through checkout, and verifies order completion.")
  void singleItemE2EPurchaseTest() {
    // 1. Login
    LoginPage loginPage = new LoginPage(page).open();
    InventoryPage inventoryPage = loginPage.performLogin("standard_user", "secret_sauce");
    inventoryPage.verifyLoaded();

    // 2. Add item to cart from Inventory Page
    ProductItemComponent backpack = inventoryPage.getProductItemByName(PRODUCT_1_NAME);
    assertThat(backpack.getPrice()).isEqualTo(PRODUCT_1_PRICE);
    backpack.addToCart();
    assertThat(inventoryPage.getHeader().getCartItemCount())
        .as("Cart count should be 1 after adding item")
        .isEqualTo(1);
    assertThat(backpack.isRemoveButtonVisible())
        .as("'Remove' button should be visible for item on inventory page after adding to cart")
        .isTrue();

    // 3. Go to Cart
    CartPage cartPage = inventoryPage.getHeader().goToCart();
    cartPage.verifyLoaded();
    assertThat(cartPage.getAllCartItems())
        .as("Cart should contain 1 item")
        .hasSize(1);
    CartItemComponent cartBackpack = cartPage.getCartItemByName(PRODUCT_1_NAME);
    assertThat(cartBackpack.getPrice()).isEqualTo(PRODUCT_1_PRICE);
    assertThat(cartBackpack.getQuantity()).isEqualTo(1);

    // 4. Proceed to Checkout Step One (Your Information)
    CheckoutStepOnePage stepOnePage = cartPage.clickCheckout();
    stepOnePage.verifyLoaded();
    stepOnePage.fillInformation(FIRST_NAME, LAST_NAME, POSTAL_CODE);

    // 5. Proceed to Checkout Step Two (Overview)
    CheckoutStepTwoPage stepTwoPage = stepOnePage.clickContinue();
    stepTwoPage.verifyLoaded();
    assertThat(stepTwoPage.getAllOverviewItems())
        .as("Overview should show 1 item")
        .hasSize(1);
    CartItemComponent overviewBackpack = stepTwoPage.getAllOverviewItems().get(0);
    assertThat(overviewBackpack.getName()).isEqualTo(PRODUCT_1_NAME);
    assertThat(overviewBackpack.getPrice()).isEqualTo(PRODUCT_1_PRICE);
    assertThat(stepTwoPage.getItemTotalText()).contains(PRODUCT_1_PRICE.replace("$", ""));

    // 6. Finish Checkout
    CheckoutCompletePage completePage = stepTwoPage.clickFinish();
    completePage.verifyLoaded();
    assertThat(completePage.getCompleteHeaderText())
        .as("Completion header message check")
        .isEqualTo("Thank you for your order!");
    assertThat(completePage.getCompleteText())
        .as("Completion body text check")
        .contains("Your order has been dispatched");

    // 7. Go Back Home (to Inventory)
    InventoryPage inventoryPageAfterPurchase = completePage.clickBackHome();
    inventoryPageAfterPurchase.verifyLoaded();
    assertThat(inventoryPageAfterPurchase.getHeader().getCartItemCount())
        .as("Cart count should be 0 after completing purchase and returning home")
        .isEqualTo(0);
  }


  @Test
  @Severity(SeverityLevel.CRITICAL)
  @Story("Multiple Items Purchase and Modification")
  @DisplayName("Verify purchase flow with multiple items, removing one from cart")
  @Description("User adds multiple items, views cart, removes one item, then completes checkout for the remaining item.")
  void multipleItemsAndRemovalPurchaseTest() {
    // 1. Login
    LoginPage loginPage = new LoginPage(page).open();
    InventoryPage inventoryPage = loginPage.performLogin("standard_user", "secret_sauce");
    inventoryPage.verifyLoaded();

    // 2. Add multiple items
    inventoryPage.getProductItemByName(PRODUCT_1_NAME).addToCart();
    inventoryPage.getProductItemByName(PRODUCT_2_NAME).addToCart();
    assertThat(inventoryPage.getHeader().getCartItemCount())
        .as("Cart count should be 2 after adding two items")
        .isEqualTo(2);

    // 3. Go to Cart
    CartPage cartPage = inventoryPage.getHeader().goToCart();
    cartPage.verifyLoaded();
    List<CartItemComponent> itemsInCart = cartPage.getAllCartItems();
    assertThat(itemsInCart.size())
        .as("Cart should contain 2 items")
        .isEqualTo(2);
    assertThat(itemsInCart.stream().map(CartItemComponent::getName).toList())
        .as("Cart should contain the correct items by name")
        .containsExactlyInAnyOrder(PRODUCT_1_NAME, PRODUCT_2_NAME);

    // 4. Remove one item from cart (e.g., PRODUCT_2_NAME)
    cartPage.removeItemByName(PRODUCT_2_NAME);
    assertThat(cartPage.getHeader().getCartItemCount()) // Check header count update
        .as("Cart count in header should be 1 after removing an item")
        .isEqualTo(1);
    assertThat(cartPage.getAllCartItems().size())
        .as("Cart should now contain 1 item after removal")
        .isEqualTo(1);
    assertThat(cartPage.getCartItemByName(PRODUCT_1_NAME).isVisible())
        .as("Remaining item (PRODUCT_1_NAME) should still be in cart")
        .isTrue();

    // 5. Proceed to Checkout Step One with remaining item
    CheckoutStepOnePage stepOnePage = cartPage.clickCheckout();
    stepOnePage.verifyLoaded();
    stepOnePage.fillInformation(FIRST_NAME, LAST_NAME, POSTAL_CODE);

    // 6. Proceed to Checkout Step Two (Overview)
    CheckoutStepTwoPage stepTwoPage = stepOnePage.clickContinue();
    stepTwoPage.verifyLoaded();
    assertThat(stepTwoPage.getAllOverviewItems())
        .as("Overview should show 1 item (the remaining one)")
        .hasSize(1);
    assertThat(stepTwoPage.getAllOverviewItems().get(0).getName())
        .as("Overview item name check")
        .isEqualTo(PRODUCT_1_NAME);
    assertThat(stepTwoPage.getItemTotalText()).contains(PRODUCT_1_PRICE.replace("$", ""));

    // 7. Finish Checkout
    CheckoutCompletePage completePage = stepTwoPage.clickFinish();
    completePage.verifyLoaded();
    assertThat(completePage.getCompleteHeaderText())
        .isEqualTo("Thank you for your order!");

    // 8. Go Back Home
    InventoryPage inventoryPageAfterPurchase = completePage.clickBackHome();
    inventoryPageAfterPurchase.verifyLoaded();
    assertThat(inventoryPageAfterPurchase.getHeader().getCartItemCount())
        .as("Cart count should be 0 after purchase")
        .isEqualTo(0);
  }
}