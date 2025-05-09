package com.nazarov.saucedemo;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.nazarov.saucedemo.config.AppConfig;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PlaywrightManager {

  private static final ThreadLocal<Playwright> PLAYWRIGHT = ThreadLocal.withInitial(() -> {
    var playwright = Playwright.create();
    playwright.selectors().setTestIdAttribute(AppConfig.get().getTestIdAttribute());
    log.info("Playwright instance created");
    return playwright;
  });

  private static final ThreadLocal<Browser> BROWSER = ThreadLocal.withInitial(() -> {
    var options = new BrowserType.LaunchOptions()
        .setHeadless(AppConfig.get().getHeadless())
        .setSlowMo(AppConfig.get().getSlowMo());
    BrowserType browserType = switch (AppConfig.get().getBrowser()) {
      case CHROME -> PLAYWRIGHT.get().chromium();
      case FIREFOX -> PLAYWRIGHT.get().firefox();
      case SAFARI -> PLAYWRIGHT.get().webkit();
      default -> throw new IllegalArgumentException(
          "Unsupported browser: " + AppConfig.get().getBrowser());
    };
    log.info("Browser instance created: {}", AppConfig.get().getBrowser());
    return browserType.launch(options);
  });

  private static final ThreadLocal<BrowserContext> BROWSER_CONTEXT = ThreadLocal.withInitial(() -> {
    var contextOptions = new Browser.NewContextOptions();
    if (Boolean.TRUE.equals(AppConfig.get().getRecordVideo())) {
      contextOptions.setRecordVideoDir(Paths.get(getVideoPath()));
    }
    var context = BROWSER.get().newContext(contextOptions);
    context.setDefaultTimeout(AppConfig.get().getLocatorTimeout());
    log.info("BrowserContext instance created with timeout: {}",
        AppConfig.get().getLocatorTimeout());
    return context;
  });

  private static final ThreadLocal<Page> PAGE = ThreadLocal.withInitial(() -> {
    var page = BROWSER_CONTEXT.get().newPage();
    log.info("Page instance created");
    return page;
  });

  private static final ThreadLocal<String> VIDEO_PATH = ThreadLocal.withInitial(() -> {
    var path = String.format("target/videos/%s", UUID.randomUUID());
    log.info("Video path set: {}", path);
    return path;
  });

  private static final ThreadLocal<String> TRACE_PATH = ThreadLocal.withInitial(() -> {
    var path = String.format("target/trace/%s", UUID.randomUUID());
    log.info("Trace path set: {}", path);
    return path;
  });

  private PlaywrightManager() {
  }

  public static BrowserContext getBrowserContext() {
    return BROWSER_CONTEXT.get();
  }

  public static Page getPage() {
    return PAGE.get();
  }

  public static String getVideoPath() {
    return VIDEO_PATH.get();
  }

  public static String getTracePath() {
    return TRACE_PATH.get();
  }

  public static void cleanVideoPath() {
    VIDEO_PATH.remove();
    log.info("Video path cleaned");
  }

  public static void cleanTracePath() {
    TRACE_PATH.remove();
    log.info("Trace path cleaned");
  }

  public static void closePage() {
    try {
      if (PAGE.get() != null) {
        PAGE.get().close();
        PAGE.remove();
        log.info("Page closed");
      }
      if (BROWSER_CONTEXT.get() != null) {
        BROWSER_CONTEXT.get().close();
        BROWSER_CONTEXT.remove();
        log.info("BrowserContext closed");
      }
      if (BROWSER.get() != null) {
        BROWSER.get().close();
        BROWSER.remove();
        log.info("Browser closed");
      }
      if (PLAYWRIGHT.get() != null) {
        PLAYWRIGHT.get().close();
        PLAYWRIGHT.remove();
        log.info("Playwright instance closed");
      }
    } catch (Exception e) {
      log.error("Error while closing resources", e);
    }
  }
}
