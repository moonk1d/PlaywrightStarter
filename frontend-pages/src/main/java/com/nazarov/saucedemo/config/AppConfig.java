package com.nazarov.saucedemo.config;

import com.nazarov.saucedemo.BrowserTypes;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Slf4j
@Getter
@ComponentScan({"com.nazarov.saucedemo"})
@PropertySource({"classpath:application-${env:dev}.properties"})
public class AppConfig {

  private static AppConfig config;

  @Value("${baseUri}")
  private String baseUri;

  @Value("${pw.browser}")
  private BrowserTypes browser;

  @Value("${pw.testIdAttribute}")
  private String testIdAttribute;

  @Value("${pw.browser.headless}")
  private Boolean headless;

  @Value("${pw.browser.slowMo}")
  private Double slowMo;

  @Value("${pw.video.record}")
  private Boolean recordVideo;

  @Value("${pw.locator.timeout}")
  private Double locatorTimeout;

  public AppConfig() {
    AppConfig.config = this;
  }

  public static AppConfig get() {
    return config;
  }

}
