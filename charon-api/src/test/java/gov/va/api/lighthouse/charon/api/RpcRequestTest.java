package gov.va.api.lighthouse.charon.api;

import static gov.va.api.lighthouse.charon.api.RoundTrip.assertRoundTrip;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RpcRequestTest {

  static Stream<Arguments> roundTrip() {
    return Stream.of(arguments(v0()), arguments(v1()));
  }

  private static RpcRequest v0() {
    return RpcRequest.builder()
        .principal(RpcPrincipal.builder().accessCode("ac").verifyCode("vc").build())
        .target(
            RpcVistaTargets.builder()
                .forPatient("p1")
                .include(List.of("1"))
                .exclude(List.of("2"))
                .build())
        .rpc(
            RpcDetails.builder()
                .name("FAUX NAME")
                .context("FAUX CONTEXT")
                .parameters(
                    List.of(
                        RpcDetails.Parameter.builder().string("").build(),
                        RpcDetails.Parameter.builder().string("a").build(),
                        RpcDetails.Parameter.builder().ref("").build(),
                        RpcDetails.Parameter.builder().ref("b").build(),
                        RpcDetails.Parameter.builder().array(List.of()).build(),
                        RpcDetails.Parameter.builder().array(List.of("c")).build(),
                        RpcDetails.Parameter.builder().namedArray(Map.of()).build(),
                        RpcDetails.Parameter.builder().namedArray(Map.of("d", "e")).build()))
                .build())
        .build();
  }

  private static RpcRequest v1() {
    return RpcRequest.builder()
        .principal(RpcPrincipal.builder().accessCode("ac").verifyCode("vc").build())
        .target(
            RpcVistaTargets.builder()
                .forPatient("p1")
                .include(List.of("1"))
                .exclude(List.of("2"))
                .build())
        .siteSpecificPrincipals(
            Map.of(
                "123",
                RpcPrincipal.applicationProxyUserBuilder()
                    .applicationProxyUser("A1")
                    .accessCode("ac1")
                    .verifyCode("vc1")
                    .build(),
                "456",
                RpcPrincipal.standardUserBuilder().accessCode("ac2").verifyCode("vc2").build()))
        .rpc(
            RpcDetails.builder()
                .name("FAUX NAME")
                .context("FAUX CONTEXT")
                .parameters(
                    List.of(
                        RpcDetails.Parameter.builder().string("").build(),
                        RpcDetails.Parameter.builder().string("a").build(),
                        RpcDetails.Parameter.builder().ref("").build(),
                        RpcDetails.Parameter.builder().ref("b").build(),
                        RpcDetails.Parameter.builder().array(List.of()).build(),
                        RpcDetails.Parameter.builder().array(List.of("c")).build(),
                        RpcDetails.Parameter.builder().namedArray(Map.of()).build(),
                        RpcDetails.Parameter.builder().namedArray(Map.of("d", "e")).build()))
                .build())
        .build();
  }

  @ParameterizedTest
  @MethodSource
  void roundTrip(RpcRequest sample) {
    assertRoundTrip(sample);
  }
}
