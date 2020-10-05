package gov.va.api.health.vistalink.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class NoOpIT {

  @Test
  public void noOperation() {
    String url = System.getProperty("integration.vistalink.url", "Not-Found");
    String apiPath = System.getProperty("integration.vistalink.api-path", "Not-Found");
    log.info("Integration Tests Running: {}/{}", url, apiPath);
  }
}
