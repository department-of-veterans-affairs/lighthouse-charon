package gov.va.api.lighthouse.vistalink.tests;

import static org.junit.Assume.assumeTrue;

import gov.va.api.lighthouse.vistalink.service.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class BadRpcContextIT {
  @Test
  @SneakyThrows
  void requestForbiddenRpcContext() {
    var systemDefinition = SystemDefinitions.get();
    assumeTrue(systemDefinition.isVistalinkAvailable());
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
        TestClients.rpcRequest(systemDefinition.vistalink().apiPath() + "rpc", body)
            .expect(403)
            .expectValid(RpcResponse.class);
    log.info(response.toString());
  }
}
