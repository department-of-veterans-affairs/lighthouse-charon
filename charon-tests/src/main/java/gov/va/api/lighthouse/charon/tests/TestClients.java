package gov.va.api.lighthouse.charon.tests;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.health.sentinel.BasicTestClient;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.health.sentinel.TestClient;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestClients {
  Map<String, String> headers() {
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    SystemDefinitions.get().clientKey().ifPresent(key -> headers.put("client-key", key));
    return headers;
  }

  ExpectedResponse rpcRequest(String path, RpcRequest body) {
    return TestClients.charon().post(headers(), path, body);
  }

  TestClient charon() {
    return BasicTestClient.builder()
        .service(SystemDefinitions.get().charon())
        .mapper(JacksonConfig::createMapper)
        .contentType("application/json")
        .build();
  }
}
