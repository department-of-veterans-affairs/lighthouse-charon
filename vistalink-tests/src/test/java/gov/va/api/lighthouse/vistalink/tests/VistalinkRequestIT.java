package gov.va.api.lighthouse.vistalink.tests;

import static org.junit.Assume.assumeTrue;

import gov.va.api.lighthouse.vistalink.service.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class VistalinkRequestIT {

  private static SystemDefinition systemDefinition = SystemDefinitions.get();

  @Test
  void requestRpcWithGlobalArrayArgument() {
    requestRpcWithValidResponse(systemDefinition.testRpcs().globalArrayRequestRpc());
  }

  @Test
  void requestRpcWithLocalArrayArgument() {
    requestRpcWithValidResponse(systemDefinition.testRpcs().localArrayRequestRpc());
  }

  @Test
  void requestRpcWithStringArgument() {
    requestRpcWithValidResponse(systemDefinition.testRpcs().stringRequestRpc());
  }

  @SneakyThrows
  void requestRpcWithValidResponse(RpcDetails rpc) {
    assumeTrue(systemDefinition.isVistalinkAvailable());
    log.info(rpc.name());
    RpcRequest body =
        RpcRequest.builder()
            .rpc(rpc)
            .principal(systemDefinition.testRpcPrincipal())
            .target(systemDefinition.testTargets())
            .build();
    var response =
        TestClients.rpcRequest(systemDefinition.vistalink().apiPath() + "rpc", body)
            .expect(200)
            .expectValid(RpcResponse.class);
    log.info(response.toString());
  }
}
