package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.service.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@Slf4j
public class VistalinkRpcInvokerTest {

  VistalinkTestConfig config;
  VistalinkRpcInvokerFactory vistalinkRpcInvokerFactory;

  @Test
  @EnabledIfSystemProperty(named = "test.vistalink", matches = "true")
  void invoke() {
    config = VistalinkTestConfig.fromSystemProperties();
    vistalinkRpcInvokerFactory = new VistalinkRpcInvokerFactory();
    var rpcPrincipal =
        RpcPrincipal.builder().accessCode(config.accessCode).verifyCode(config.verifyCode).build();
    var connectionDetails =
        ConnectionDetails.builder()
            .divisionIen(config.divisionIen)
            .port(Integer.parseInt(config.port))
            .host(config.host)
            .name(config.name)
            .build();
    var vistalinkRpcInvoker = vistalinkRpcInvokerFactory.create(rpcPrincipal, connectionDetails);
    RpcInvocationResult result =
        vistalinkRpcInvoker.invoke(
            RpcDetails.builder().name("XOBV TEST PING").context("XOBV VISTALINK TESTER").build());
    log.info("RESULT\n---\n{}\n---", result.response());
  }

  @Value
  @Builder
  static class VistalinkTestConfig {
    String accessCode;
    String verifyCode;
    String divisionIen;
    String host;
    String port;
    String name;

    @SneakyThrows
    static VistalinkTestConfig fromSystemProperties() {
      String host = propertyOrDie("host");

      return VistalinkTestConfig.builder()
          .accessCode(propertyOrDie("access-code"))
          .verifyCode(propertyOrDie("verify-code"))
          .divisionIen(propertyOrDie("division-ien"))
          .host(host)
          .port(propertyOrDie("port"))
          .name(System.getProperty("vlx.name", "vlx:" + host))
          .build();
    }

    private static String propertyOrDie(String suffix) {
      var propertyName = "vlx." + suffix;
      var value = System.getProperty(propertyName);
      if (value == null || value.isBlank()) {
        throw new IllegalStateException("Missing property: " + propertyName);
      }
      return value;
    }
  }
}
