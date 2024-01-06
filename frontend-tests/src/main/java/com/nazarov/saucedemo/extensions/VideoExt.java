package com.nazarov.saucedemo.extensions;

import com.nazarov.saucedemo.PW;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
public class VideoExt implements AfterEachCallback {

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    PW pw = SpringExtension.getApplicationContext(context).getBean(PW.class);
    if (Boolean.TRUE.equals(pw.getConfig().getRecordVideo())) {
      File dir = new File(pw.getVideoPath());

      if (context.getExecutionException().isPresent()) {
        List<File> videos = Arrays.asList(Objects.requireNonNull(dir.listFiles()));
        log.info("Test failed - attaching video file");
        videos.forEach(video -> {
          try {
            byte[] videoBytes = Files.readAllBytes(Paths.get(video.getPath()));
            Allure.addAttachment("video", "video/webm",
                new ByteArrayInputStream(videoBytes), ".webm");
          } catch (IOException e) {
            log.error("Allure video attachment failed", e);
          }
        });
      }
      FileUtils.deleteDirectory(dir);
    }
  }
}
