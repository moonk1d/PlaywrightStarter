package com.nazarov.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.pages.components.InventoryItem;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ProductsPage extends SauceDemoPage {

  private Locator title;
  private List<InventoryItem> inventoryItems;

  public ProductsPage(Page page) {
    super(page);
    title = page.locator("//span[@class='title']");
    inventoryItems = page.locator("//div[@class='inventory_item']")
        .all().stream()
        .map(InventoryItem::new)
        .toList();
  }

  @Override
  public ProductsPage open() {
    page.navigate(
        "https://www.saucedemo.com/inventory.html"); // TODO add mature routing implementation
    return this;
  }
}
