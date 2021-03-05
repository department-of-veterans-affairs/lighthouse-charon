package gov.va.api.lighthouse.charon.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentIn;
import static org.hamcrest.CoreMatchers.equalTo;

import gov.va.api.health.sentinel.Environment;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
public class HealthCheckIT {

  @ParameterizedTest
  @ValueSource(strings = {"/", "/charon/"})
  void healthCheckIsUnprotected(String basePath) {
    assumeEnvironmentIn(Environment.LOCAL);
    var requestPath = basePath + "actuator/health";
    log.info("Running health-check for path: {}", requestPath);
    TestClients.charon().get(requestPath).response().then().body("status", equalTo("UP"));
  }
}
