package gov.va.api.lighthouse.vistalink.tests;

import static org.junit.Assume.assumeTrue;

import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import gov.va.api.lighthouse.vistalink.service.api.RpcVistaTargets;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnknownVistaIT {
  @Test
  @SneakyThrows
  void requestUnkonwnVistaWith400() {
    var systemDefinition = SystemDefinitions.get();
    assumeTrue(systemDefinition.isVistalinkAvailable());
    RpcRequest body =
        RpcRequest.builder()
            .rpc(systemDefinition.testRpcs().pingRpc())
            .principal(systemDefinition.testRpcPrincipal())
            .target(RpcVistaTargets.builder().include(List.of("who dis")).build())
            .build();
    var response =
        TestClients.rpcRequest(systemDefinition.vistalink().apiPath() + "rpc", body)
            .expect(400)
            .expectValid(RpcResponse.class);
    log.info(response.toString());
  }
}
