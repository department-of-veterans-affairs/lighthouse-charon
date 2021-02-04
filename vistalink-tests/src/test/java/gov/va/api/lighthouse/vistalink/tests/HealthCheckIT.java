package gov.va.api.lighthouse.vistalink.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentIn;
import static org.hamcrest.CoreMatchers.equalTo;

import gov.va.api.health.sentinel.Environment;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
public class HealthCheckIT {

  @ParameterizedTest
  @ValueSource(strings = {"/", "/vistalink/"})
  void healthCheckIsUnprotected(String basePath) {
    // Local Testing Only
    assumeEnvironmentIn(Environment.LOCAL);
    TestClients.vistalink()
        .get(basePath + "actuator/health")
        .response()
        .then()
        .body("status", equalTo("UP"));
  }
}
