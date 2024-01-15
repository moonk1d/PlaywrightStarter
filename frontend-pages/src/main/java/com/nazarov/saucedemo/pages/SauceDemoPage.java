package com.nazarov.saucedemo.pages;

import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.config.AppConfig;

public abstract class SauceDemoPage {

  protected Page page;
  protected static final String BASE_URL = AppConfig.get().getBaseUri();

  protected SauceDemoPage(Page page) {
    this.page = page;
  }

  public abstract SauceDemoPage open();
}
