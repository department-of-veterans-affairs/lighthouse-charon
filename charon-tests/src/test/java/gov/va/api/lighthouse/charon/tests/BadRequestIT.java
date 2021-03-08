package gov.va.api.lighthouse.charon.tests;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import gov.va.api.lighthouse.charon.api.RpcResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class BadRequestIT {

  @Test
  @SneakyThrows
  void requestInvalidBodyResponseWith400() {
    var systemDefinition = SystemDefinitions.get();
    assumeTrue(systemDefinition.isVistaAvailable(), "Vista is unavailable.");
    String body = "{\"message\": \"Im a malformed request.\"}";
    var response =
        TestClients.charon()
            .post(TestClients.headers(), systemDefinition.charon().apiPath() + "rpc", body)
            .expect(400)
            .expectValid(RpcResponse.class);
    log.info(response.toString());
  }
}
