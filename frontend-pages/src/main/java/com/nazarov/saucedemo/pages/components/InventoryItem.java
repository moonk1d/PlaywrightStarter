package com.nazarov.saucedemo.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class InventoryItem {

  private Locator image;
  private Locator title;
  private Locator description;
  private Locator price;
  private Locator addCartButton;

  public InventoryItem(Locator locator) {
    image = locator.locator("//div[@class='inventory_item_img']");
    title = locator.locator("//div[@class='inventory_item_name ']");
    description = locator.locator("//div[@class='inventory_item_desc']");
    price = locator.locator("//div[@class='inventory_item_price']");
    addCartButton = locator.getByRole(AriaRole.BUTTON);
  }

}
