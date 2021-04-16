package gov.va.api.lighthouse.charon.tests;

import static gov.va.api.lighthouse.charon.tests.TestOptions.assumeVistaIsAvailable;

import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnauthorizedIT {

  @Test
  @SneakyThrows
  void requestFailedLoginResponseWith401() {
    assumeVistaIsAvailable();
    RpcRequest body =
        RpcRequest.builder()
            .rpc(SystemDefinitions.get().testRpcs().pingRpc())
            .principal(
                RpcPrincipal.builder()
                    .accessCode("I'm sorry Dave")
                    .verifyCode("I'm afraid I can't do that")
                    .build())
            .target(SystemDefinitions.get().testTargets())
            .build();
    var response =
        TestClients.rpcRequest(SystemDefinitions.get().charon().apiPath() + "rpc", body)
            .expect(401)
            .expectValid(RpcResponse.class);
    log.info(response.toString());
  }
}
