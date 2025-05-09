package com.nazarov.saucedemo.utils;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AllureAttachment {

  public void attachLog(String name, byte[] logContent) {
    Allure.addAttachment(name, "text/plain", new ByteArrayInputStream(logContent), ".txt");
  }

  public void attachVideo(InputStream videoContent) {
    Allure.addAttachment("Playwright video", "video/webm", videoContent, ".webm");
  }

  public void attachTrace(InputStream traceStream) {
    Allure.addAttachment("Playwright trace", "application/zip", traceStream, ".zip");
  }

  public void attachScreenshot(InputStream screenshotStream) {
    Allure.addAttachment("Screenshot", "image/png", screenshotStream, ".png");
  }


}