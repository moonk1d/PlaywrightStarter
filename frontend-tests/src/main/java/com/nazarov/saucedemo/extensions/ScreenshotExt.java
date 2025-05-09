package com.nazarov.saucedemo.extensions;

import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.PlaywrightManager;
import com.nazarov.saucedemo.config.AppConfig;
import com.nazarov.saucedemo.utils.AllureAttachment;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class ScreenshotExt implements AfterTestExecutionCallback {

  private final AllureAttachment allureAttachment = new AllureAttachment();

  @Override
  public void afterTestExecution(ExtensionContext context) {
    if (isScreenshotRecordingEnabled()) {
      handleTraceAttachment(context);
    }
  }

  private void handleTraceAttachment(ExtensionContext context) {
    if (context.getExecutionException().isPresent()) {
      log.info("Test failed - attaching screenshot");

      try {
        attachScreenshotToAllure();
      } catch (IOException e) {
        log.error("Failed to attach screenshot to Allure report", e);
      }
    }
  }

  private boolean isScreenshotRecordingEnabled() {
    return Boolean.TRUE.equals(AppConfig.get().getRecordScreenshot());
  }

  private void attachScreenshotToAllure() throws IOException {
    byte[] attachment = PlaywrightManager.getPage()
        .screenshot(new Page.ScreenshotOptions().setFullPage(true));
    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(attachment)) {
      allureAttachment.attachScreenshot(inputStream);
      log.info("Screenshot attached to Allure report");
    }
  }

}
