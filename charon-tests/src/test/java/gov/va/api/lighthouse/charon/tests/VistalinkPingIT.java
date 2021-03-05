package gov.va.api.lighthouse.charon.tests;

import static org.junit.Assume.assumeTrue;

import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class VistalinkPingIT {

  @Test
  @SneakyThrows
  void requestRpcNoArguments() {
    var systemDefinition = SystemDefinitions.get();
    assumeTrue(systemDefinition.isVistalinkAvailable());
    RpcRequest body =
        RpcRequest.builder()
            .rpc(systemDefinition.testRpcs().pingRpc())
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
