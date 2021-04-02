package gov.va.api.lighthouse.charon.service.controller;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.config.VistalinkProperties;
import gov.va.api.lighthouse.charon.service.controller.VistaLinkExceptions.UnknownVista;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NameResolutionTest {
  static Function<RpcVistaTargets, Collection<String>> additional(int... numbers) {
    return t -> names(numbers);
  }

  private static ConnectionDetails connection(int n) {
    return ConnectionDetails.builder()
        .name("v" + n)
        .host("v" + n + ".com")
        .port(8000 + n)
        .divisionIen("" + n)
        .timezone("America/New_York")
        .build();
  }

  private static List<ConnectionDetails> connections(int... numbers) {
    return Arrays.stream(numbers).mapToObj(NameResolutionTest::connection).collect(toList());
  }

  private static List<String> names(int... numbers) {
    return Arrays.stream(numbers).mapToObj((int n) -> "v" + n).collect(toList());
  }

  private static VistalinkProperties properties() {
    return VistalinkProperties.builder()
        .vistas(List.of(connection(1), connection(2), connection(3), connection(4), connection(5)))
        .build();
  }

  static Stream<Arguments> resolve() {
    return Stream.of(
        arguments(target(names(), names()), additional(), connections()),
        arguments(target(names(1), names()), additional(), connections(1)),
        arguments(target(names(1, 2, 3), names()), additional(), connections(1, 2, 3)),
        arguments(target(names(1, 2, 3), names(2)), additional(), connections(1, 3)),
        arguments(target(names(1, 2, 3), names(1, 2, 3)), additional(), connections()),
        arguments(target(names(1, 2, 3), names()), additional(4), connections(1, 2, 3, 4)),
        arguments(target(names(1, 2, 3), names(4)), additional(4), connections(1, 2, 3)),
        arguments(target(names(1, 2, 3), names()), additional(99), connections(1, 2, 3)),
        arguments(target(names(), names()), additional(99), connections()),
        arguments(
            target(List.of("localhost:18673:673:America/New_York"), names()),
            additional(),
            List.of(
                ConnectionDetails.builder()
                    .name("localhost:18673:673:America/New_York")
                    .host("localhost")
                    .port(18673)
                    .divisionIen("673")
                    .timezone("America/New_York")
                    .build())),
        arguments(
            target(List.of("v1", "localhost:18673:673:America/New_York"), names()),
            additional(2),
            List.of(
                connection(1),
                connection(2),
                ConnectionDetails.builder()
                    .name("localhost:18673:673:America/New_York")
                    .host("localhost")
                    .port(18673)
                    .divisionIen("673")
                    .timezone("America/New_York")
                    .build())));
  }

  static Stream<Arguments> resolveThrowsUnknownVistaExceptionForUnknownIncludedOrExcludedNames() {
    return Stream.of(
        arguments(target(names(99), names()), additional()),
        arguments(target(names(), names(99)), additional()));
  }

  private static RpcVistaTargets target(List<String> in, List<String> ex) {
    return RpcVistaTargets.builder().include(in).exclude(ex).build();
  }

  @ParameterizedTest
  @MethodSource
  void resolve(
      RpcVistaTargets target,
      Function<RpcVistaTargets, Collection<String>> additionalCandidates,
      List<ConnectionDetails> expected) {
    var actual =
        NameResolution.builder()
            .properties(properties())
            .additionalCandidates(additionalCandidates)
            .build()
            .resolve(target);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @ParameterizedTest
  @MethodSource
  void resolveThrowsUnknownVistaExceptionForUnknownIncludedOrExcludedNames(RpcVistaTargets target) {
    assertThatExceptionOfType(UnknownVista.class)
        .isThrownBy(
            () ->
                NameResolution.builder()
                    .properties(properties())
                    .additionalCandidates(additional())
                    .build()
                    .resolve(target));
  }
}
