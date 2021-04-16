package gov.va.api.lighthouse.charon.service.controller;

import static gov.va.api.lighthouse.charon.service.controller.InteractiveTestSupport.emptyMacroProcessorFactory;
import static gov.va.api.lighthouse.charon.service.controller.InteractiveTestSupport.localTampaConnectionDetails;
import static gov.va.api.lighthouse.charon.service.controller.InteractiveTestSupport.requirePropertyValue;
import static gov.va.api.lighthouse.charon.service.controller.InteractiveTestSupport.toRpcRequest;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.med.vistalink.rpc.RpcRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@Slf4j
class StandardUserVistalinkSessionTest {
  private RpcPrincipal principal() {
    return RpcPrincipal.builder()
        .accessCode(requirePropertyValue("standard.access-code"))
        .verifyCode(requirePropertyValue("standard.verify-code"))
        .build();
  }

  private RpcDetails rpc() {
    RpcDetails rpc =
        RpcDetails.builder().context("XOBV VISTALINK TESTER").name("XOBV TEST PING").build();
    return rpc;
  }

  @Test
  @EnabledIfSystemProperty(named = "interactive", matches = "true")
  @SneakyThrows
  void rpcInvoker() {

    RpcPrincipal principal = principal();

    RpcInvoker invoker =
        VistalinkRpcInvoker.builder()
            .connectionDetails(localTampaConnectionDetails())
            .rpcPrincipal(principal)
            .macroProcessorFactory(emptyMacroProcessorFactory())
            .build();

    RpcInvocationResult result = invoker.invoke(rpc());

    log.info("{}", result);

    assertThat(result.response()).containsIgnoringCase("ping successful");
  }

  @Test
  @EnabledIfSystemProperty(named = "interactive", matches = "true")
  @SneakyThrows
  void standardUserSession() {

    RpcPrincipal principal = principal();

    StandardUserVistalinkSession session =
        StandardUserVistalinkSession.builder()
            .connectionDetails(localTampaConnectionDetails())
            .accessCode(principal.accessCode())
            .verifyCode(principal.verifyCode())
            .build();

    RpcRequest request = toRpcRequest(rpc());
    var response = session.connection().executeRPC(request);
    log.info("{}", response.getRawResponse());
    session.close();
    assertThat(response.getRawResponse()).containsIgnoringCase("ping successful");

    var demo = session.userDemographics();
    log.info("{}", demo);
    assertThat(demo).containsKey("DUZ");
  }
}
