package gov.va.api.lighthouse.vistalink.tests;

import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class HealthCheckIT {

  private static SystemDefinition systemDefinition = SystemDefinitions.get();

  @Test
  @SneakyThrows
  void healthCheckIsUnprotected() {
    var response =
        TestClients.vistalink()
            .get(
                Map.of("Content-Type", "application/json"),
                systemDefinition.vistalink().url() + "actuator/health")
            .expect(200)
            .expectValid(String.class);
    log.info(response);
  }
}
