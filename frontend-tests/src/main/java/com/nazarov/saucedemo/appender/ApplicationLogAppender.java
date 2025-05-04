package com.nazarov.saucedemo.appender;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.ArrayList;
import java.util.List;

public class ApplicationLogAppender extends AppenderBase<ILoggingEvent> {

  private static final ThreadLocal<List<String>> threadLocal = new ThreadLocal<>();
  private final PatternLayout layout = new PatternLayout();

  @Override
  public void start() {
    super.start();
    layout.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
    layout.setContext(getContext());
    layout.start();
  }

  @Override
  public void append(ILoggingEvent e) {
    List<String> events = threadLocal.get();
    if (events == null) {
      events = new ArrayList<>();
      threadLocal.set(events);
    }
    events.add(layout.doLayout(e));
  }

  public static List<String> getEvents() {
    return threadLocal.get();
  }

  public static void clearEvents() {
    threadLocal.remove();
  }
}