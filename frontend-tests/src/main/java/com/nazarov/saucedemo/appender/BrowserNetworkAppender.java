package com.nazarov.saucedemo.appender;

import com.microsoft.playwright.ConsoleMessage;
import java.util.ArrayList;
import java.util.List;

public class BrowserNetworkAppender {

  private static final ThreadLocal<List<Object>> threadLocal = new ThreadLocal<>();

  private BrowserNetworkAppender() {
  }

  public static void append(Object networkEvent) {
    List<Object> events = threadLocal.get();
    if (events == null) {
      events = new ArrayList<>();
      threadLocal.set(events);
    }
    events.add(networkEvent);
  }

  public static List<Object> getEvents() {
    return threadLocal.get();
  }

  public static void clearEvents() {
    threadLocal.remove();
  }
}