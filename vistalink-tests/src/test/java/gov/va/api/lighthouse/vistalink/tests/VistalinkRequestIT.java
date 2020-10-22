package gov.va.api.lighthouse.vistalink.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentNotIn;
import static gov.va.api.lighthouse.vistalink.tests.SystemDefinitions.systemDefinition;
import static org.junit.jupiter.api.Assertions.fail;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class VistalinkRequestIT {

  @Test
  @SneakyThrows
  void requestRpcNoArguements() {
    assumeEnvironmentNotIn(Environment.LOCAL);
    String accessCode = System.getProperty("vista-access-code", "not-set");
    String verifyCode = System.getProperty("vista-verify-code", "not-set");
    var body =
        RpcRequest.builder()
            .rpc(systemDefinition().testRpcs().pingRpc())
            .principal(RpcPrincipal.builder().accessCode(accessCode).verifyCode(verifyCode).build())
            .build();
    TestClients.vistalink().post("rpc", body).expect(200);
  }

  @Test
  void requestRpcWithStringArguement() {
    assumeEnvironmentNotIn(Environment.LOCAL);
    fail();
  }
}
