package com.nazarov.saucedemo;

import static java.util.Objects.isNull;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.nazarov.saucedemo.config.AppConfig;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Supplier;

public final class PlaywrightManager {

  private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
  private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
  private static final ThreadLocal<BrowserContext> BROWSER_CONTEXT = new ThreadLocal<>();
  private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();
  private static final ThreadLocal<String> VIDEO_PATH = new ThreadLocal<>();

  private PlaywrightManager(){}

  public static synchronized BrowserContext getBrowserContext() {
    if (isNull(PLAYWRIGHT.get())) {
      var playwright = playwrightSupplier.get();
      PLAYWRIGHT.set(playwright);
      BROWSER.set(browserSupplier.get());
      BROWSER_CONTEXT.set(browserContextSupplier.get());
    }

    return BROWSER_CONTEXT.get();
  }

  public static synchronized Page getPage() {
    if (isNull(BROWSER_CONTEXT.get())) {
      PAGE.set(getBrowserContext().newPage());
    }

    return PAGE.get();
  }

  public static String getVideoPath() {
    if (isNull(VIDEO_PATH.get())) {
      VIDEO_PATH.set(String.format("target/videos/%s", UUID.randomUUID()));
    }
    return VIDEO_PATH.get();
  }

  public static synchronized void cleanVideoPath() { VIDEO_PATH.remove();}

  public static synchronized void closePage() {
    Playwright playwright = PLAYWRIGHT.get();
    Browser browser = BROWSER.get();
    BrowserContext browserContext = BROWSER_CONTEXT.get();
    Page page = PAGE.get();
    if (playwright != null) {
      page.close();
      PAGE.remove();
      browserContext.close();
      BROWSER_CONTEXT.remove();
      browser.close();
      BROWSER.remove();
      playwright.close();
      PLAYWRIGHT.remove();
    }
  }


  private static final Supplier<Playwright> playwrightSupplier = () -> {
    var playwright = Playwright.create();
    playwright.selectors().setTestIdAttribute(AppConfig.get().getTestIdAttribute());
    return playwright;
  };

  private static final Supplier<Browser> browserSupplier = () -> {
    BrowserType browser;
    var options = new BrowserType.LaunchOptions()
        .setHeadless(AppConfig.get().getHeadless())
        .setSlowMo(AppConfig.get().getSlowMo());
    switch (AppConfig.get().getBrowser()) {
      case CHROME -> browser = PLAYWRIGHT.get().chromium();
      case FIREFOX -> browser = PLAYWRIGHT.get().firefox();
      case SAFARI -> browser = PLAYWRIGHT.get().webkit();
      default -> throw new IllegalArgumentException();
    }
    return browser.launch(options);
  };

  private static final Supplier<BrowserContext> browserContextSupplier = () -> {
    NewContextOptions contextOptions = new Browser.NewContextOptions();
    if (Boolean.TRUE.equals(AppConfig.get().getRecordVideo())) {
      contextOptions.setRecordVideoDir(Paths.get(getVideoPath()));
    }

    var context = BROWSER.get().newContext(contextOptions);
    context.setDefaultTimeout(AppConfig.get().getLocatorTimeout());
    return context;
  };

}
