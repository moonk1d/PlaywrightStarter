package com.nazarov.saucedemo.extensions;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;
import com.nazarov.saucedemo.PlaywrightManager;
import com.nazarov.saucedemo.config.AppConfig;
import com.nazarov.saucedemo.utils.AllureAttachment;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class TraceExt implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private final AllureAttachment allureAttachment = new AllureAttachment();

  @Override
  public void beforeTestExecution(ExtensionContext extensionContext) {
    if (isTraceRecordingEnabled()) {
      startTracing();
    }
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    if (isTraceRecordingEnabled()) {
      stopTracing();
      if (context.getExecutionException().isPresent()) {
        handleTraceAttachment(context);
      }
      cleanUpTraceResources();
    }
  }

  private boolean isTraceRecordingEnabled() {
    return Boolean.TRUE.equals(AppConfig.get().getRecordTrace());
  }

  private void startTracing() {
    BrowserContext context = PlaywrightManager.getBrowserContext();
    context.tracing().start(new Tracing.StartOptions()
        .setScreenshots(true)
        .setSnapshots(true)
        .setSources(true)
    );
    log.info("Tracing started for the test");
  }

  private void stopTracing() {
    Path tracePath = Paths.get(PlaywrightManager.getTracePath());
    BrowserContext context = PlaywrightManager.getBrowserContext();
    context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));
    log.info("Tracing stopped and saved to {}", tracePath);
  }

  private void handleTraceAttachment(ExtensionContext context) {
    if (context.getExecutionException().isPresent()) {
      log.info("Test failed - attaching trace file");
      try {
        attachTraceToAllure();
      } catch (IOException e) {
        log.error("Failed to attach trace to Allure report", e);
      }
    }
  }

  private void attachTraceToAllure() throws IOException {
    Path tracePath = Paths.get(PlaywrightManager.getTracePath());
    try (ByteArrayInputStream traceStream = new ByteArrayInputStream(Files.readAllBytes(tracePath))) {
      allureAttachment.attachTrace(traceStream);
      log.info("Trace file attached to Allure report");
    }
  }

  private void cleanUpTraceResources() throws IOException {
    Path tracePath = Paths.get(PlaywrightManager.getTracePath());
    File dir = tracePath.getParent().toFile();
    FileUtils.deleteDirectory(dir);
    PlaywrightManager.cleanTracePath();
    log.info("Trace resources cleaned up");
  }
}