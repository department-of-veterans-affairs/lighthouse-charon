package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.config.VistalinkProperties;
import java.util.List;
import org.assertj.core.api.Assertions;
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
    Assertions.assertThat(resolver.resolve(RpcVistaTargets.builder().build()))
        .containsExactlyInAnyOrder(details);
  }

  @Test
  void onlyKnownExcludesAreNotReturnedWhenSpecified() {
    Assertions.assertThat(
            resolver.resolve(RpcVistaTargets.builder().exclude(List.of("v1", "v3")).build()))
        .containsExactlyInAnyOrderElementsOf(List.of(details[0], details[2], details[4]));
  }

  @Test
  void onlyKnownIncludesAreReturnedWhenSpecified() {
    Assertions.assertThat(
            resolver.resolve(RpcVistaTargets.builder().include(List.of("v1", "v3")).build()))
        .containsExactlyInAnyOrderElementsOf(List.of(details[1], details[3]));
  }

  @Test
  void unknownVistaExceptionIsThrownForIncludedUnknownVista() {
    assertThatExceptionOfType(VistaLinkExceptions.UnknownVista.class)
        .isThrownBy(
            () ->
                resolver.resolve(
                    RpcVistaTargets.builder()
                        .forPatient("123V456")
                        .include(List.of("nope"))
                        .exclude(List.of("v1"))
                        .build()));
    assertThatExceptionOfType(VistaLinkExceptions.UnknownVista.class)
        .isThrownBy(
            () ->
                resolver.resolve(
                    RpcVistaTargets.builder()
                        .forPatient("123V456")
                        .include(List.of("v1"))
                        .exclude(List.of("nope"))
                        .build()));
    assertThatExceptionOfType(VistaLinkExceptions.UnknownVista.class)
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
