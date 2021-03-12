package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class VistalinkSessionTest {
  static Stream<Arguments> connectionIdentifier() {
    var bigBoi = deets(Long.MAX_VALUE + "nope");
    return Stream.of(
        arguments(deets("123"), 123),
        arguments(deets("a123b"), 9712398),
        arguments(deets("ab"), 9798),
        arguments(bigBoi, bigBoi.hashCode()));
  }

  static ConnectionDetails deets(String name) {
    return ConnectionDetails.builder().name(name).build();
  }

  @ParameterizedTest
  @MethodSource
  void connectionIdentifier(ConnectionDetails deets, long expected) {
    assertThat(VistalinkSession.connectionIdentifier(deets)).isEqualTo(expected);
  }
}
