package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assumptions.assumeThat;

import gov.va.api.lighthouse.vistalink.service.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import lombok.Builder;
import lombok.Value;
import org.junit.jupiter.api.Test;

public class VistalinkRpcInvokerTest {

  VistalinkTestConfig config = VistalinkTestConfig.fromSystemProperties();
  VistalinkRpcInvokerFactory vistalinkRpcInvokerFactory = new VistalinkRpcInvokerFactory();

  @Test
  void invoke() {
    assumeThat(System.getProperty("test.vistalink")).isEqualTo("true");

    var rpcPrincipal =
        RpcPrincipal.builder().accessCode(config.accessCode).verifyCode(config.verifyCode).build();
    var connectionDetails =
        ConnectionDetails.builder()
            .divisionIen(config.divisionIen)
            .port(config.port)
            .host(config.host)
            .name(config.name)
            .build();
    var vistalinkRpcInvoker = vistalinkRpcInvokerFactory.create(rpcPrincipal, connectionDetails);
    vistalinkRpcInvoker.invoke(
        RpcDetails.builder().name("XOBV TEST PING").context(config.rpcContext).build());
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
    String rpcContext;

    static VistalinkTestConfig fromSystemProperties() {
      String host = propertyOrDie("host");
      return VistalinkTestConfig.builder()
          .accessCode(propertyOrDie("access-code"))
          .verifyCode(propertyOrDie("verify-code"))
          .divisionIen(propertyOrDie("division-ien"))
          .rpcContext(propertyOrDie("rpc-context"))
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
