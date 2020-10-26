package gov.va.api.lighthouse.vistalink.tests;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.SentinelProperties;
import gov.va.api.health.sentinel.ServiceDefinition;
import gov.va.api.lighthouse.vistalink.service.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.service.api.RpcDetails.Parameter;
import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.BooleanUtils;

@UtilityClass
public class SystemDefinitions {
  /** Return the applicable system definition for the current environment. */
  static SystemDefinition get() {
    switch (Environment.get()) {
      case LOCAL:
        return local();
      case QA:
        return qa();
      default:
        throw new IllegalArgumentException(
            "Unsupported sentinel environment: " + Environment.get());
    }
  }

  private static SystemDefinition local() {
    String url = "http://localhost";
    return SystemDefinition.builder()
        .vistalink(serviceDefinition("vistalink", url, 8050, ""))
        .testRpcs(rpcs())
        .testRpcPrincipal(rpcPrincipal())
        .isVistalinkAvailable(BooleanUtils.toBoolean(System.getProperty("test.vistalink", "false")))
        .build();
  }

  private static SystemDefinition qa() {
    String url = "http://blue.qa.lighthouse.va.gov";
    return SystemDefinition.builder()
        .vistalink(serviceDefinition("vistalink", url, 443, "/vistalink/"))
        .testRpcs(rpcs())
        .testRpcPrincipal(rpcPrincipal())
        .isVistalinkAvailable(true)
        .build();
  }

  private static TestRpcs rpcs() {
    return TestRpcs.builder()
        .pingRpc(
            RpcDetails.builder().context("XOBV VISTALINK TESTER").name("XOBV TEST PING").build())
        .stringRequestRpc(
            RpcDetails.builder()
                .context("XOBV VISTALINK TESTER")
                .name("XOBV TEST STRING")
                .parameters(List.of(Parameter.builder().string("SHANKTOPUS GO!").build()))
                .build())
        .build();
  }

  private RpcPrincipal rpcPrincipal() {
    return RpcPrincipal.builder()
        .accessCode(System.getProperty("vista.access-code", "not-set"))
        .verifyCode(System.getProperty("vista.verify-code", "not-set"))
        .build();
  }

  private static ServiceDefinition serviceDefinition(
      String name, String url, int port, String apiPath) {
    return ServiceDefinition.builder()
        .url(SentinelProperties.optionUrl(name, url))
        .port(port)
        .apiPath(SentinelProperties.optionApiPath(name, apiPath))
        .accessToken(() -> Optional.empty())
        .build();
  }
}
