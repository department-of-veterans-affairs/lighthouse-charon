package gov.va.api.lighthouse.charon.service.controller;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PrincipalResolutionTest {

  static Stream<Arguments> resolve() {
    return Stream.of(
        arguments(request(), target("a"), principal("default")),
        arguments(request("b", "c"), target("a"), principal("default")),
        arguments(request("b", "c"), target("b"), principal("b")),
        arguments(request("b", "c"), target("c"), principal("c"))
        //
        );
  }

  static RpcRequest request(String... specific) {
    return RpcRequest.builder()
        .principal(principal("default"))
        .siteSpecificPrincipals(
            Stream.of(specific).collect(toMap(identity(), PrincipalResolutionTest::principal)))
        .rpc(RpcDetails.builder().name("WHATEVER").context("WHATEVER CTX").build())
        .target(RpcVistaTargets.builder().forPatient("WHOEVER").build())
        .build();
  }

  static RpcPrincipal principal(String name) {
    return RpcPrincipal.standardUserBuilder()
        .accessCode("ac-" + name)
        .verifyCode("vc-" + name)
        .build();
  }

  static ConnectionDetails target(String name) {
    return ConnectionDetails.builder()
        .name(name)
        .divisionIen("1")
        .host(name + ".com")
        .port(8888)
        .timezone("America/New_York")
        .build();
  }

  @ParameterizedTest
  @MethodSource
  void resolve(RpcRequest request, ConnectionDetails target, RpcPrincipal expected) {
    assertThat(PrincipalResolution.of(request).resolve(target)).isEqualTo(expected);
  }
}
