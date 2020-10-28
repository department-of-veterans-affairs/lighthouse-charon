package gov.va.api.lighthouse.vistalink.tests;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.health.sentinel.BasicTestClient;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.health.sentinel.TestClient;
import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class TestClients {
  ExpectedResponse rpcRequest(String path, RpcRequest body) {
    return TestClients.vistalink().post(Map.of("Content-Type", "application/json"), path, body);
  }

  TestClient vistalink() {
    return BasicTestClient.builder()
        .service(SystemDefinitions.get().vistalink())
        .mapper(JacksonConfig::createMapper)
        .build();
  }
}
