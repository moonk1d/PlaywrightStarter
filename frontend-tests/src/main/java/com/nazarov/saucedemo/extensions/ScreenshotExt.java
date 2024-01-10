package com.nazarov.saucedemo.extensions;

import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.PlaywrightManager;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class ScreenshotExt implements AfterTestExecutionCallback {

  @Override
  public void afterTestExecution(ExtensionContext context) {
    if (context.getExecutionException().isPresent()) {
      log.info("Test failed - attaching screenshot");

      byte[] attachment = PlaywrightManager.getPage()
          .screenshot(new Page.ScreenshotOptions().setFullPage(true));
      Allure.addAttachment("screenshot", new ByteArrayInputStream(attachment));
    }
  }

}
