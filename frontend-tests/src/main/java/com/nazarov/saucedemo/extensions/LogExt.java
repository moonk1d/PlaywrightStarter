package com.nazarov.saucedemo.extensions;

import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.PlaywrightManager;
import com.nazarov.saucedemo.appender.BrowserConsoleLogAppender;
import com.nazarov.saucedemo.appender.BrowserNetworkAppender;
import com.nazarov.saucedemo.config.AppConfig;
import com.nazarov.saucedemo.utils.AllureAttachment;
import com.nazarov.saucedemo.utils.ApplicationLogManager;
import com.nazarov.saucedemo.utils.ConsoleLogManager;
import com.nazarov.saucedemo.utils.NetworkLogManager;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class LogExt implements BeforeEachCallback, AfterEachCallback {

  private final ApplicationLogManager applicationLogManager = new ApplicationLogManager();
  private final ConsoleLogManager consoleLogManager = new ConsoleLogManager();
  private final NetworkLogManager networkLogManager = new NetworkLogManager();
  private final AllureAttachment allureAttachment = new AllureAttachment();

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    if (isLogRecordingEnabled()) {
      Page page = PlaywrightManager.getPage();
      page.onConsoleMessage(BrowserConsoleLogAppender::append);
      page.onRequest(BrowserNetworkAppender::append);
      page.onResponse(BrowserNetworkAppender::append);
    }
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    if (isLogRecordingEnabled()) {
      allureAttachment.attachLog("ApplicationLog", applicationLogManager.getApplicationLog());
      allureAttachment.attachLog("BrowserConsoleLog", consoleLogManager.getConsoleLog());
      allureAttachment.attachLog("BrowserNetworkLog", networkLogManager.getNetworkLog());
    }
  }

  private boolean isLogRecordingEnabled() {
    return Boolean.TRUE.equals(AppConfig.get().getRecordLog());
  }
}