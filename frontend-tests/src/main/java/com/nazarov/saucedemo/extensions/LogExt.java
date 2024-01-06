package com.nazarov.saucedemo.extensions;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class LogExt implements AfterTestExecutionCallback {

  @Override
  public void afterTestExecution(ExtensionContext context) throws IOException {
    File logFile = new File("target/logs/testFile.log");
    byte[] log = Files.readAllBytes(Paths.get(logFile.getPath()));
    Allure.addAttachment("log", "text/plain", new ByteArrayInputStream(log), ".log");
    FileUtils.write(logFile, "", Charset.defaultCharset());
  }

}
