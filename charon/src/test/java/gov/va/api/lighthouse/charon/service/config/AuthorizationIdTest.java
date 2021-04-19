package gov.va.api.lighthouse.charon.service.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class AuthorizationIdTest {

  static Stream<Arguments> wellFormattedStringParses() {
    return Stream.of(
        arguments("123", "456"),
        arguments("abc", "def"),
        arguments("123", "456@whatever@is@all@site"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"nope", "nope@", "@nope", "nope:nope", "nope nope"})
  void poorlyFormattedStringThrowsException(String nope) {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> AuthorizationId.of(nope));
  }

  @Test
  void toStringIsPretty() {
    assertThat(AuthorizationId.builder().duz("d1").site("s1").build().toString())
        .isEqualTo("d1@s1");
  }

  @ParameterizedTest
  @MethodSource
  void wellFormattedStringParses(String duz, String site) {
    assertThat(AuthorizationId.of(duz + "@" + site))
        .isEqualTo(AuthorizationId.builder().duz(duz).site(site).build());
  }
}
