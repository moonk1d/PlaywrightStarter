package com.nazarov.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ProductsPage extends SauceDemoPage {

  private Locator title;

  public ProductsPage(Page page) {
    super(page);
    title = page.locator("//span[@class='title']");
  }

  @Override
  public ProductsPage open() {
    page.navigate("https://www.saucedemo.com/inventory.html"); // TODO add mature routing implementation
    return this;
  }
}
