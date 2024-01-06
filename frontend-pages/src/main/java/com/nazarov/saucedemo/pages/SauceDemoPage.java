package com.nazarov.saucedemo.pages;

import com.microsoft.playwright.Page;

public abstract class SauceDemoPage {

  protected Page page;

  protected SauceDemoPage(Page page) {
    this.page = page;
  }

  public abstract SauceDemoPage open();
}
