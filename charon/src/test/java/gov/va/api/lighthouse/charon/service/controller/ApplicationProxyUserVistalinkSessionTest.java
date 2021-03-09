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
import gov.va.med.vistalink.rpc.RpcResponse;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@Slf4j
class ApplicationProxyUserVistalinkSessionTest {

  @Test
  @EnabledIfSystemProperty(named = "interactive", matches = "true")
  @SneakyThrows
  void applicationProxyUserSession() {
    RpcPrincipal principal = principal();

    VistalinkSession session =
        ApplicationProxyUserVistalinkSession.builder()
            .applicationProxyUser(principal.applicationProxyUser())
            .accessCode(principal.accessCode())
            .verifyCode(principal.verifyCode())
            .connectionDetails(localTampaConnectionDetails())
            .build();

    RpcRequest request = toRpcRequest(rpc());
    RpcResponse response = session.connection().executeRPC(request);
    log.info("{}", response.getRawResponse());
    session.close();
    assertThat(response.getRawResponse()).containsIgnoringCase("blood pressure");
  }

  private RpcPrincipal principal() {
    return RpcPrincipal.builder()
        .accessCode(requirePropertyValue("app-proxy.access-code"))
        .verifyCode(requirePropertyValue("app-proxy.verify-code"))
        .applicationProxyUser("LHS,APPLICATION PROXY")
        .build();
  }

  private RpcDetails rpc() {
    return RpcDetails.builder()
        .context("LHS RPC CONTEXT")
        .name("VPR GET PATIENT DATA")
        .parameters(
            List.of(
                RpcDetails.Parameter.builder().string("140").build(),
                RpcDetails.Parameter.builder().string("vitals").build(),
                RpcDetails.Parameter.builder().string("").build(),
                RpcDetails.Parameter.builder().string("").build(),
                RpcDetails.Parameter.builder().string("").build(),
                RpcDetails.Parameter.builder().string("").build(),
                RpcDetails.Parameter.builder().array(List.of()).build()))
        .build();
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

    assertThat(result.response()).containsIgnoringCase("blood pressure");
  }
}
