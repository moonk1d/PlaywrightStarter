package com.nazarov.saucedemo.appender;

import com.microsoft.playwright.ConsoleMessage;
import java.util.ArrayList;
import java.util.List;

public class BrowserConsoleLogAppender {

  private static final ThreadLocal<List<ConsoleMessage>> threadLocal = new ThreadLocal<>();

  private BrowserConsoleLogAppender() {
  }

  public static void append(ConsoleMessage consoleMessage) {
    List<ConsoleMessage> events = threadLocal.get();
    if (events == null) {
      events = new ArrayList<>();
      threadLocal.set(events);
    }
    events.add(consoleMessage);
  }

  public static List<ConsoleMessage> getEvents() {
    return threadLocal.get();
  }

  public static void clearEvents() {
    threadLocal.remove();
  }
}