package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GetInsEntryParserTest {
  static Stream<Arguments> parseLine() {
    return Stream.of(
        arguments("1^2^3^4^5", GetInsEntry.of("1", "2", "3", "4", "5")),
        arguments("1^2^3^4^", GetInsEntry.of("1", "2", "3", "4", null)),
        arguments("1^2^3^^5", GetInsEntry.of("1", "2", "3", null, "5")),
        arguments("1^2^3^^", GetInsEntry.of("1", "2", "3", null, null)));
  }

  static Stream<Arguments> parseLineFailureThrows() {
    return Stream.of(
        arguments(IllegalArgumentException.class, null),
        arguments(IllegalArgumentException.class, "1^2^3^4"),
        arguments(IllegalArgumentException.class, "1^2^3^4^5^6"),
        arguments(NullPointerException.class, "^2^3^4^5"),
        arguments(NullPointerException.class, "1^^3^4^5"),
        arguments(NullPointerException.class, "1^2^^4^5"));
  }

  @ParameterizedTest
  @MethodSource
  void parseLine(String sample, GetInsEntry expected) {
    assertThat(GetInsEntryParser.create().parseLine(sample)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource
  void parseLineFailureThrows(Class exception, String sample) {
    assertThatExceptionOfType(exception)
        .isThrownBy(() -> GetInsEntryParser.create().parseLine(sample));
  }

  @Test
  void parseNewLineDelimited() {
    assertThat(GetInsEntryParser.create().parseNewLineDelimited("1^2^3^4^5\na^b^c^d^e"))
        .containsExactlyInAnyOrder(
            GetInsEntry.of("1", "2", "3", "4", "5"), GetInsEntry.of("a", "b", "c", "d", "e"));
  }
}
