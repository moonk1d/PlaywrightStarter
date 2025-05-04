package com.nazarov.saucedemo.utils;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;

public class AllureLogAttachment {

  public void attachLog(String name, byte[] logContent) {
    Allure.addAttachment(name, "text/plain", new ByteArrayInputStream(logContent), ".txt");
  }
}