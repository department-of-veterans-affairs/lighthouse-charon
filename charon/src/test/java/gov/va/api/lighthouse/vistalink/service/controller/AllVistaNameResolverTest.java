package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gov.va.api.lighthouse.vistalink.api.RpcVistaTargets;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.api.lighthouse.vistalink.service.config.VistalinkProperties;
import gov.va.api.lighthouse.vistalink.service.controller.VistaLinkExceptions.UnknownVista;
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

  @Test
  void onlyKnownExcludesAreNotReturnedWhenSpecified() {
    assertThat(resolver.resolve(RpcVistaTargets.builder().exclude(List.of("v1", "v3")).build()))
        .containsExactlyInAnyOrderElementsOf(List.of(details[0], details[2], details[4]));
  }

  @Test
  void onlyKnownIncludesAreReturnedWhenSpecified() {
    assertThat(resolver.resolve(RpcVistaTargets.builder().include(List.of("v1", "v3")).build()))
        .containsExactlyInAnyOrderElementsOf(List.of(details[1], details[3]));
  }

  @Test
  void unknownVistaExceptionIsThrownForIncludedUnknownVista() {
    assertThatExceptionOfType(UnknownVista.class)
        .isThrownBy(
            () ->
                resolver.resolve(
                    RpcVistaTargets.builder()
                        .forPatient("123V456")
                        .include(List.of("nope"))
                        .exclude(List.of("v1"))
                        .build()));
    assertThatExceptionOfType(UnknownVista.class)
        .isThrownBy(
            () ->
                resolver.resolve(
                    RpcVistaTargets.builder()
                        .forPatient("123V456")
                        .include(List.of("v1"))
                        .exclude(List.of("nope"))
                        .build()));
    assertThatExceptionOfType(UnknownVista.class)
        .isThrownBy(
            () ->
                resolver.resolve(
                    RpcVistaTargets.builder()
                        .forPatient("123V456")
                        .include(List.of("nope"))
                        .exclude(List.of("alsonope"))
                        .build()));
  }
}
