package gov.va.api.lighthouse.vistalink.tests;

import static gov.va.api.lighthouse.vistalink.tests.SystemDefinitions.systemDefinition;
import static gov.va.api.lighthouse.vistalink.tests.VistalinkRequest.vistalinkRequest;

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
    var body =
        RpcRequest.builder()
            .rpc(systemDefinition().testRpcs().stringRequestRpc())
            .principal(
                RpcPrincipal.builder()
                    .accessCode(VistalinkProperties.vistaAccessCode())
                    .verifyCode(VistalinkProperties.vistaVerifyCode())
                    .build())
            .build();
    var response = vistalinkRequest(body).expect(200).expectValid(RpcResponse.class);
    log.info(response.toString());
  }
}
