package com.nazarov.saucedemo.extensions;

import com.microsoft.playwright.Page;
import com.nazarov.saucedemo.PlaywrightManager;
import com.nazarov.saucedemo.appender.BrowserConsoleLogAppender;
import com.nazarov.saucedemo.appender.BrowserNetworkAppender;
import com.nazarov.saucedemo.utils.AllureLogAttachment;
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
  private final AllureLogAttachment allureLogAttachment = new AllureLogAttachment();

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    Page page = PlaywrightManager.getPage();
    page.onConsoleMessage(BrowserConsoleLogAppender::append);
    page.onRequest(BrowserNetworkAppender::append);
    page.onResponse(BrowserNetworkAppender::append);
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    allureLogAttachment.attachLog("ApplicationLog", applicationLogManager.getApplicationLog());
    allureLogAttachment.attachLog("BrowserConsoleLog", consoleLogManager.getConsoleLog());
    allureLogAttachment.attachLog("BrowserNetworkLog", networkLogManager.getNetworkLog());
  }
}