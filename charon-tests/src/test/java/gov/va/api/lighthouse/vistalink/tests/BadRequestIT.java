package gov.va.api.lighthouse.vistalink.tests;

import static org.junit.Assume.assumeTrue;

import gov.va.api.lighthouse.vistalink.api.RpcResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class BadRequestIT {

  @Test
  @SneakyThrows
  void requestInvalidBodyResponseWith400() {
    var systemDefinition = SystemDefinitions.get();
    assumeTrue(systemDefinition.isVistalinkAvailable());
    String body = "{\"message\": \"Im a malformed request.\"}";
    var response =
        TestClients.vistalink()
            .post(TestClients.headers(), systemDefinition.vistalink().apiPath() + "rpc", body)
            .expect(400)
            .expectValid(RpcResponse.class);
    log.info(response.toString());
  }
}
