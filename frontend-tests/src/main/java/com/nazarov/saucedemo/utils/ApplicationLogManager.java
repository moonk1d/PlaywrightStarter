package com.nazarov.saucedemo.utils;

import com.nazarov.saucedemo.appender.ApplicationLogAppender;
import java.util.List;

public class ApplicationLogManager {

  private static final byte[] EMPTY_LOG = "Log output is empty".getBytes();

  public byte[] getApplicationLog() {
    List<String> logEvents = ApplicationLogAppender.getEvents();
    return logEvents != null ? String.join("", logEvents).getBytes() : EMPTY_LOG;
  }
}