package gov.va.api.lighthouse.charon.tests;

import static org.junit.Assume.assumeTrue;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class RpcRequestIT {

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
    assumeTrue(systemDefinition.isVistaAvailable());
    log.info(rpc.name());
    RpcRequest body =
        RpcRequest.builder()
            .rpc(rpc)
            .principal(systemDefinition.testRpcPrincipal())
            .target(systemDefinition.testTargets())
            .build();
    var response =
        TestClients.rpcRequest(systemDefinition.charon().apiPath() + "rpc", body)
            .expect(200)
            .expectValid(RpcResponse.class);
    log.info(response.toString());
  }
}
