package gov.va.api.lighthouse.vistalink.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class NoopSmokeTestIT {

  @Test
  void noOperation() {
    String url = System.getProperty("integration.vistalink.url", "Not-Found");
    String apiPath = System.getProperty("integration.vistalink.api-path", "Not-Found");
    log.info("Integration Tests Running: {}/{}", url, apiPath);
  }
}
