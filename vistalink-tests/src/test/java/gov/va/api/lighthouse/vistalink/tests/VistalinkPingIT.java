package gov.va.api.lighthouse.vistalink.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentNotIn;
import static gov.va.api.lighthouse.vistalink.tests.SystemDefinitions.systemDefinition;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class VistalinkPingIT {

  @Test
  @SneakyThrows
  void requestRpcNoArguements() {
    assumeEnvironmentNotIn(Environment.LOCAL);
    var body =
        RpcRequest.builder()
            .rpc(systemDefinition().testRpcs().pingRpc())
            .principal(
                RpcPrincipal.builder()
                    .accessCode(VistalinkProperties.vistaAccessCode)
                    .verifyCode(VistalinkProperties.vistaVerifyCode)
                    .build())
            .build();
    TestClients.vistalink().post("rpc", body).expect(200).expectValid(RpcResponse.class);
  }
}
