package gov.va.api.lighthouse.charon.tests;

import static gov.va.api.lighthouse.charon.tests.TestOptions.assumeVistaIsAvailable;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class RpcRequestIT {

  @Test
  void requestRpcWithGlobalArrayArgument() {
    requestRpcWithValidResponse(SystemDefinitions.get().testRpcs().globalArrayRequestRpc());
  }

  @Test
  void requestRpcWithLocalArrayArgument() {
    requestRpcWithValidResponse(SystemDefinitions.get().testRpcs().localArrayRequestRpc());
  }

  @Test
  void requestRpcWithStringArgument() {
    requestRpcWithValidResponse(SystemDefinitions.get().testRpcs().stringRequestRpc());
  }

  @SneakyThrows
  void requestRpcWithValidResponse(RpcDetails rpc) {
    assumeVistaIsAvailable();
    log.info(rpc.name());
    RpcRequest body =
        RpcRequest.builder()
            .rpc(rpc)
            .principal(SystemDefinitions.get().avCodePrincipal())
            .target(SystemDefinitions.get().testTargets())
            .build();
    var response =
        TestClients.rpcRequest(SystemDefinitions.get().charon().apiPath() + "rpc", body)
            .expect(200)
            .expectValid(RpcResponse.class);
    log.info(response.toString());
  }

  @Test
  void requestWithSiteSpecificPrincipals() {
    assumeVistaIsAvailable();
    RpcRequest body =
        RpcRequest.builder()
            .rpc(SystemDefinitions.get().testRpcs().stringRequestRpc())
            .principal(
                RpcPrincipal.standardUserBuilder()
                    .accessCode("NOPE")
                    .verifyCode("ALSO_NOPE")
                    .build())
            .siteSpecificPrincipals(
                Map.of(
                    SystemDefinitions.get().vistaSite(), SystemDefinitions.get().avCodePrincipal()))
            .target(
                RpcVistaTargets.builder()
                    .include(List.of(SystemDefinitions.get().vistaSite()))
                    .build())
            .build();
    var response =
        TestClients.rpcRequest(SystemDefinitions.get().charon().apiPath() + "rpc", body)
            .expect(200)
            .expectValid(RpcResponse.class);
    log.info(response.toString());
  }
}
