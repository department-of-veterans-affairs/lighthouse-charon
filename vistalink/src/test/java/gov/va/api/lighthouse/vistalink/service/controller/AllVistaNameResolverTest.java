package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.vistalink.service.api.RpcVistaTargets;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.api.lighthouse.vistalink.service.config.VistalinkProperties;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllVistaNameResolverTest {

  ConnectionDetails[] details;
  AllVistaNameResolver resolver;

  private ConnectionDetails _connectionDetail(int n) {
    return ConnectionDetails.builder()
        .name("v" + n)
        .host("v" + n + ".com")
        .port(8000 + n)
        .divisionIen("" + n)
        .build();
  }

  @BeforeEach
  void _init() {
    details =
        new ConnectionDetails[] {
          _connectionDetail(0),
          _connectionDetail(1),
          _connectionDetail(2),
          _connectionDetail(3),
          _connectionDetail(4),
        };
    var properties = VistalinkProperties.builder().vistas(List.of(details)).build();
    resolver = new AllVistaNameResolver(properties);
  }

  @Test
  void allAreReturnedWhenNoTargetsAreSpecified() {
    assertThat(resolver.resolve(RpcVistaTargets.builder().build()))
        .containsExactlyInAnyOrder(details);
  }
}