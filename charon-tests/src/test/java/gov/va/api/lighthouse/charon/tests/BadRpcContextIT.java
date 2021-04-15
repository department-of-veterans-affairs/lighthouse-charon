package gov.va.api.lighthouse.charon.tests;

import static gov.va.api.lighthouse.charon.tests.TestOptions.assumeVistaIsAvailable;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class BadRpcContextIT {
  @Test
  @SneakyThrows
  void requestForbiddenRpcContext() {
    assumeVistaIsAvailable();

    RpcRequest body =
        RpcRequest.builder()
            .rpc(
                RpcDetails.builder()
                    .context("NOPE CONTEXT")
                    .name("VPR GET PATIENT DATA JSON")
                    .build())
            .principal(SystemDefinitions.get().testRpcPrincipal())
            .target(SystemDefinitions.get().testTargets())
            .build();
    var response =
        TestClients.rpcRequest(SystemDefinitions.get().charon().apiPath() + "rpc", body)
            .expect(403)
            .expectValid(RpcResponse.class);
  }
}
