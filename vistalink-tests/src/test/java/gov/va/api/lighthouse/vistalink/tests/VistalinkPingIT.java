package gov.va.api.lighthouse.vistalink.tests;

import static gov.va.api.lighthouse.vistalink.tests.SystemDefinitions.systemDefinition;
import static gov.va.api.lighthouse.vistalink.tests.VistalinkRequest.isEnvironmentReady;
import static gov.va.api.lighthouse.vistalink.tests.VistalinkRequest.request;

import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class VistalinkPingIT {

  @Test
  @SneakyThrows
  void requestRpcNoArguements() {
    if (isEnvironmentReady()) {
      var body =
          RpcRequest.builder()
              .rpc(systemDefinition().testRpcs().pingRpc())
              .principal(
                  RpcPrincipal.builder()
                      .accessCode(VistalinkProperties.vistaAccessCode())
                      .verifyCode(VistalinkProperties.vistaVerifyCode())
                      .build())
              .build();
      var response = request(body).expect(200).expectValid(RpcResponse.class);
      log.info(response.toString());
    }
  }
}
