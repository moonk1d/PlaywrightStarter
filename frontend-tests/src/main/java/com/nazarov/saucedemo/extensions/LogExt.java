package com.nazarov.saucedemo.extensions;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.nazarov.saucedemo.appender.LogThreadSafeAppender;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.stream.Collectors;
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
    byte[] log = LogThreadSafeAppender.getEvents().stream()
        .map(eventFormatter)
        .collect(Collectors.joining(System.lineSeparator(), "", System.lineSeparator())).getBytes();
    Allure.addAttachment("log", "text/plain", new ByteArrayInputStream(log), ".log");
  }

  private static final Function<ILoggingEvent, String> eventFormatter = event -> {
    var formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withZone(ZoneId.systemDefault());
    return formatter.format(event.getInstant())
        + " "
        + event.getLoggerName()
        + " "
        + event.getFormattedMessage();
  };
}
