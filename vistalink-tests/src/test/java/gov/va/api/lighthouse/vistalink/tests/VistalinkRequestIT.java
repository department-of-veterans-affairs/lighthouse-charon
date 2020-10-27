package gov.va.api.lighthouse.vistalink.tests;

import static org.junit.Assume.assumeTrue;

import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class VistalinkRequestIT {

  @Test
  @SneakyThrows
  void requestRpcWithStringArgument() {
    var systemDefinition = SystemDefinitions.get();
    assumeTrue(systemDefinition.isVistalinkAvailable());
    RpcRequest body =
        RpcRequest.builder()
            .rpc(systemDefinition.testRpcs().stringRequestRpc())
            .principal(systemDefinition.testRpcPrincipal())
            .build();
    var response =
        TestClients.rpcRequest(systemDefinition.vistalink().apiPath() + "/rpc", body)
            .expect(200)
            .expectValid(RpcResponse.class);
    log.info(response.toString());
  }
}
