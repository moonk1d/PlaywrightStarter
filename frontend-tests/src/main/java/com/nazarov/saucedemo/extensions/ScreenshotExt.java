package com.nazarov.saucedemo.extensions;

import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.PW;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
public class ScreenshotExt implements AfterTestExecutionCallback {

  @Override
  public void afterTestExecution(ExtensionContext context) {
    if (context.getExecutionException().isPresent()) {
      log.info("Test failed - attaching video file");

      Page page = SpringExtension.getApplicationContext(context).getBean(PW.class).getPage();
      byte[] attachment = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
      Allure.addAttachment("screenshot", new ByteArrayInputStream(attachment));
    }
  }

}
