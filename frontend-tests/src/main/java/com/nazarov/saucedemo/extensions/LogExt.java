package com.nazarov.saucedemo.extensions;

import static java.util.Objects.nonNull;

import com.nazarov.saucedemo.appender.LogThreadSafeAppender;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class LogExt implements BeforeEachCallback, AfterEachCallback {

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    LogThreadSafeAppender.clearEvents();
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    List<String> events = LogThreadSafeAppender.getEvents();
    byte[] log = "Log output is empty".getBytes();
    if (nonNull(events)) {
      log = String.join("", events).getBytes();
    }
    Allure.addAttachment("log", "text/plain", new ByteArrayInputStream(log), ".log");
  }
}
