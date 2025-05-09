package com.nazarov.saucedemo.extensions;

import com.nazarov.saucedemo.PlaywrightManager;
import com.nazarov.saucedemo.config.AppConfig;
import com.nazarov.saucedemo.utils.AllureAttachment;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class VideoExt implements BeforeTestExecutionCallback, AfterEachCallback {

  private final ThreadLocal<String> path = new ThreadLocal<>();
  private final AllureAttachment allureAttachment = new AllureAttachment();

  @Override
  public void beforeTestExecution(ExtensionContext extensionContext) {
    path.set(PlaywrightManager.getVideoPath());
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    if (shouldAttachVideo(context)) {
      attachVideoToAllure();
    }
    cleanUpResources();
  }

  private boolean shouldAttachVideo(ExtensionContext context) {
    return Boolean.TRUE.equals(AppConfig.get().getRecordVideo()) && context.getExecutionException().isPresent();
  }

  private void attachVideoToAllure() {
    try {
      File videoFile = getFirstVideoFile().orElseThrow(() -> new IOException("No video file found"));
      try (ByteArrayInputStream videoStream = new ByteArrayInputStream(Files.readAllBytes(videoFile.toPath()))) {
        allureAttachment.attachVideo(videoStream);
      }
      log.info("Video file attached to Allure report");
    } catch (IOException e) {
      log.error("Failed to attach video to Allure report", e);
    }
  }

  private Optional<File> getFirstVideoFile() {
    Path videoPath = Paths.get(path.get());
    File videoDir = videoPath.toFile();
    return Arrays.stream(Optional.ofNullable(videoDir.listFiles()).orElse(new File[0])).findFirst();
  }

  private void cleanUpResources() throws IOException {
    Path videoPath = Paths.get(path.get());
    FileUtils.deleteDirectory(videoPath.toFile());
    path.remove();
    PlaywrightManager.cleanVideoPath();
    log.info("Video resources cleaned up");
  }
}
