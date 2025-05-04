package com.nazarov.saucedemo.utils;

import com.microsoft.playwright.ConsoleMessage;
import com.nazarov.saucedemo.appender.BrowserConsoleLogAppender;
import java.util.List;

public class ConsoleLogManager {

  private static final byte[] EMPTY_CONSOLE_LOG = "Console output is empty".getBytes();

  public byte[] getConsoleLog() {
    List<ConsoleMessage> consoleMessages = BrowserConsoleLogAppender.getEvents();
    if (consoleMessages != null) {
      List<String> formattedMessages = consoleMessages.stream()
          .map(msg -> String.format("[%s] [%s] %s", msg.type(), msg.location(), msg.text()))
          .toList();
      return String.join(System.lineSeparator(), formattedMessages).getBytes();
    }
    return EMPTY_CONSOLE_LOG;
  }
}
