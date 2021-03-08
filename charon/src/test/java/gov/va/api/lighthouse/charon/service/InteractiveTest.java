package gov.va.api.lighthouse.charon.service;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.controller.DfnMacro;
import gov.va.api.lighthouse.charon.service.controller.MacroProcessorFactory;
import gov.va.api.lighthouse.charon.service.controller.VistalinkRpcInvoker;
import gov.va.api.lighthouse.charon.service.controller.VistalinkRpcInvokerFactory;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@Slf4j
public class InteractiveTest {

  private String requirePropertyValue(String name) {
    String value = System.getProperty(name);
    assertThat(value).withFailMessage("System property %s not defined", name).isNotBlank();
    return value;
  }

  @Test
  @EnabledIfSystemProperty(named = "interactive", matches = "true")
  @SneakyThrows
  void standardAccessVerifyCode() {

    RpcPrincipal principal =
        RpcPrincipal.builder()
            .accessCode(requirePropertyValue("standard.access-code"))
            .verifyCode(requirePropertyValue("standard.verify-code"))
            .build();
    ConnectionDetails connectionDetails =
        ConnectionDetails.builder()
            .name("673")
            .host("localhost")
            .port(18673)
            .divisionIen("673")
            .timezone("America/New_York")
            .build();
    RpcDetails rpc =
        RpcDetails.builder().context("XOBV VISTALINK TESTER").name("XOBV TEST PING").build();

    MacroProcessorFactory mp = new MacroProcessorFactory(List.of(new DfnMacro()));
    VistalinkRpcInvokerFactory rif = new VistalinkRpcInvokerFactory(mp);
    VistalinkRpcInvoker invoker = (VistalinkRpcInvoker) rif.create(principal, connectionDetails);
    RpcInvocationResult result = invoker.invoke(rpc);
    log.info("{}", result);
  }
}
