package com.nazarov.saucedemo;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.nazarov.saucedemo.config.AppConfig;
import java.nio.file.Paths;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInfo;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PW {

  private Playwright playwrightSession;
  private Browser browserSession;
  @Getter
  private BrowserContext browserContext;
  @Getter
  private Page page;
  @Getter
  private AppConfig config;
  private TestInfo testInfo;

  public PW(AppConfig config) {
    this.config = config;
  }

  public void init(TestInfo testInfo) {
    log.info("Initialise test session");
    this.testInfo = testInfo;
    playwrightSession = playwrightSession == null ? playwrightSupplier.get() : playwrightSession;
    browserSession = browserSession == null ? browserSupplier.get() : browserSession;
    browserContext = browserContextSupplier.get();
    page = pageSupplier.get();
  }

  private final Supplier<Playwright> playwrightSupplier = () -> {
    var playwright = Playwright.create();
    playwright.selectors().setTestIdAttribute(config.getTestIdAttribute());
    return playwright;
  };

  private final Supplier<Browser> browserSupplier = () -> {
    BrowserType browser;
    var options = new BrowserType.LaunchOptions()
        .setHeadless(config.getHeadless())
        .setSlowMo(config.getSlowMo());
    switch (config.getBrowser()) {
      case CHROME -> browser = playwrightSession.chromium();
      case FIREFOX -> browser = playwrightSession.firefox();
      case SAFARI -> browser = playwrightSession.webkit();
      default -> throw new IllegalArgumentException();
    }
    return browser.launch(options);
  };

  private final Supplier<BrowserContext> browserContextSupplier = () -> {
    NewContextOptions contextOptions = new Browser.NewContextOptions();
    if (Boolean.TRUE.equals(config.getRecordVideo())) {
      contextOptions.setRecordVideoDir(Paths.get(getVideoPath()));
    }

    var context = browserSession.newContext(contextOptions);
    context.setDefaultTimeout(config.getLocatorTimeout());
    return context;
  };

  public String getVideoPath() {
    return String.format("target/videos/%s/%s",
        testInfo.getTestClass().orElseThrow().getCanonicalName(),
        testInfo.getDisplayName());
  }

  private final Supplier<Page> pageSupplier = () -> {
    return browserContext.newPage();
  };

  public void clean() {
    log.info("Cleanup test session");
    this.page.close();
    this.browserContext.close();
    this.browserContext = null;
    this.page = null;
  }

}
