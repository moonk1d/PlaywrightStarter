package com.nazarov.saucedemo;

import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.config.AppConfig;
import com.nazarov.saucedemo.extensions.LogExt;
import com.nazarov.saucedemo.extensions.ScreenshotExt;
import com.nazarov.saucedemo.extensions.VideoExt;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(AppConfig.class)
@ExtendWith({ScreenshotExt.class, VideoExt.class, LogExt.class})
@Slf4j
public class TestFixture {

  protected Page page;

  @BeforeEach
  void beforeEach() {
    page = PlaywrightManager.getPage();
  }

  @AfterEach
  void afterEach() {
    PlaywrightManager.closePage();
  }

}
