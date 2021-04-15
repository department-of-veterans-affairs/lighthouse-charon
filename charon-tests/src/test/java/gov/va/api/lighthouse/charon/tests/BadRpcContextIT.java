package gov.va.api.lighthouse.charon.tests;

import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.fail;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class BadRpcContextIT {
  @Test
  @SneakyThrows
  void requestForbiddenRpcContext() {
    fail();
    var systemDefinition = SystemDefinitions.get();
    // Ur boi (Joshy-Boi) can't get this to return a 403, only a 200 with an error message
    assumeTrue(false);
    RpcRequest body =
        RpcRequest.builder()
            .rpc(
                // Build a request where context does not match name
                RpcDetails.builder()
                    .context("XOBV VISTALINK TESTER")
                    .name("VPR GET PATIENT DATA JSON")
                    .build())
            .principal(systemDefinition.testRpcPrincipal())
            .target(systemDefinition.testTargets())
            .build();
    log.info(body.toString());
    var response =
        TestClients.rpcRequest(systemDefinition.charon().apiPath() + "rpc", body)
            .expect(403)
            .expectValid(RpcResponse.class);
    log.info(response.toString());
  }
}
