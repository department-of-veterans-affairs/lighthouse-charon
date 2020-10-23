package gov.va.api.lighthouse.vistalink.tests;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.health.sentinel.BasicTestClient;
import gov.va.api.health.sentinel.TestClient;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestClients {
  TestClient vistalink() {
    return BasicTestClient.builder()
        .service(SystemDefinitions.systemDefinition().vistalink())
        .mapper(JacksonConfig::createMapper)
        .build();
  }
}
