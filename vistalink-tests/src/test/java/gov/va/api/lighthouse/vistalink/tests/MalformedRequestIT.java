package gov.va.api.lighthouse.vistalink.tests;

import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class MalformedRequestIT {

  @Test
  @SneakyThrows
  void requestInvalidBodyResponseWith400() {
    var systemDefinition = SystemDefinitions.get();
    String body = "Im a malformed request.";
    var response =
        TestClients.vistalink()
            .post(
                Map.of("Content-Type", "application/json"),
                systemDefinition.vistalink().apiPath() + "rpc",
                body)
            .expect(400)
            .expectValid(RpcResponse.class);
    log.info(response.toString());
  }
}
