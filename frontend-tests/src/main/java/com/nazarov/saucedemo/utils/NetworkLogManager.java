package com.nazarov.saucedemo.utils;

import com.microsoft.playwright.Request;
import com.microsoft.playwright.Response;
import com.nazarov.saucedemo.appender.BrowserNetworkAppender;
import java.util.List;

public class NetworkLogManager {

  private static final byte[] EMPTY_NETWORK_LOG = "Network output is empty".getBytes();

  public byte[] getNetworkLog() {
    List<Object> networkEvents = BrowserNetworkAppender.getEvents();
    if (networkEvents != null) {
      List<String> formattedEvents = networkEvents.stream()
          .map(this::formatNetworkEvent)
          .toList();
      return String.join(System.lineSeparator(), formattedEvents).getBytes();
    }
    return EMPTY_NETWORK_LOG;
  }

  private String formatNetworkEvent(Object event) {
    if (event instanceof Request request) {
      return String.format(
          "Request:%nMethod: %s%nURL: %s%nHeaders: %s%nPost Data: %s",
          request.method(),
          request.url(),
          request.headers(),
          request.postData() != null ? request.postData() : "N/A"
      );
    } else if (event instanceof Response response) {
      return String.format(
          "Response:%nStatus: %d%nURL: %s%nHeaders: %s",
          response.status(),
          response.url(),
          response.headers()
      );
    }
    return "Unknown network event";
  }
}