package gov.va.api.lighthouse.vistalink.tests;

import static gov.va.api.lighthouse.vistalink.tests.SystemDefinitions.systemDefinition;
import static org.junit.Assume.assumeTrue;

import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class VistalinkRequestIT {

  @Test
  @SneakyThrows
  void requestRpcWithStringArguement() {
    assumeTrue(SystemDefinitions.isVistalinkAvailable());
    RpcRequest body =
        RpcRequest.builder()
            .rpc(systemDefinition().testRpcs().stringRequestRpc())
            .principal(
                RpcPrincipal.builder()
                    .accessCode(TestClients.vistaAccessCode())
                    .verifyCode(TestClients.vistaVerifyCode())
                    .build())
            .build();
    var response = TestClients.request(body).expect(200).expectValid(RpcResponse.class);
    log.info(response.toString());
  }
}
