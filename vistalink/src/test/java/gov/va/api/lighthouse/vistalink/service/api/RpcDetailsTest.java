package gov.va.api.lighthouse.vistalink.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.lighthouse.vistalink.service.api.RpcDetails.Parameter;
import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RpcDetailsTest {

  static Stream<Arguments> emptyStringsAreParsedFromJsonAsEmptyStrings() {
    return Stream.of(
        Arguments.of(
            "{\"name\":\"MICKY\",\"context\":\"SO FINE\",\"parameters\":[{\"string\":\"\"}]}",
            RpcDetails.builder()
                .name("MICKY")
                .context("SO FINE")
                .parameters(List.of(Parameter.builder().string("").build()))
                .build()),
        Arguments.of(
            "{\"name\":\"MICKY\",\"context\":\"SO FINE\",\"parameters\":[{\"ref\":\"\"}]}",
            RpcDetails.builder()
                .name("MICKY")
                .context("SO FINE")
                .parameters(List.of(Parameter.builder().ref("").build()))
                .build()),
        Arguments.of(
            "{\"name\":\"MICKY\",\"context\":\"SO FINE\",\"parameters\":[{\"array\":[\"a\",\"\"]}]}",
            RpcDetails.builder()
                .name("MICKY")
                .context("SO FINE")
                .parameters(List.of(Parameter.builder().array(List.of("a", "")).build()))
                .build()));
  }

  @SneakyThrows
  @ParameterizedTest
  @MethodSource
  void emptyStringsAreParsedFromJsonAsEmptyStrings(String json, RpcDetails expected) {
    RpcDetails details = JacksonConfig.createMapper().readValue(json, RpcDetails.class);
    assertThat(details).isEqualTo(expected);
  }

  @Test
  void parameterToStringReturnsClassAndType() {
    assertThat(RpcDetails.Parameter.builder().ref("ONE FUGAZI").build().toString())
        .isEqualTo("Parameter(ref=ONE FUGAZI)");
  }

  @Test
  void parameterTypeReturnsCorrectType() {
    assertThat(RpcDetails.Parameter.builder().ref("ONE FUGAZI").build().type()).isEqualTo("ref");
    assertThat(RpcDetails.Parameter.builder().string("TWO FUGAZI").build().type())
        .isEqualTo("string");
    assertThat(
            RpcDetails.Parameter.builder()
                .array(List.of("RED FUGAZI", "BLUE FUGAZI"))
                .build()
                .type())
        .isEqualTo("array");
  }

  @Test
  void parameterTypeThrowsIllegalStateExceptionOnUnknownType() {
    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(
            () -> {
              var ok = RpcDetails.Parameter.builder().string("ok").build();
              var bad = ok.string(null);
              bad.type();
            });
  }

  @Test
  void parameterValueReturnsCorrectValue() {
    assertThat(RpcDetails.Parameter.builder().ref("ONE FUGAZI").build().value())
        .isEqualTo("ONE FUGAZI");
    assertThat(RpcDetails.Parameter.builder().string("TWO FUGAZI").build().value())
        .isEqualTo("TWO FUGAZI");
    assertThat(
            RpcDetails.Parameter.builder()
                .array(List.of("RED FUGAZI", "BLUE FUGAZI"))
                .build()
                .value())
        .isEqualTo(List.of("RED FUGAZI", "BLUE FUGAZI"));
  }

  @Test
  void parameterValueThrowsIllegalStateExceptionOnUnknownType() {
    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(
            () -> {
              var ok = RpcDetails.Parameter.builder().string("ok").build();
              var bad = ok.string(null);
              bad.value();
            });
  }

  @Test
  void parametersMustContainOnlyOneType() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(
            () ->
                RpcDetails.builder()
                    .context("rhymes")
                    .name("Dr. Seuss")
                    .parameters(
                        List.of(
                            RpcDetails.Parameter.builder()
                                .ref("ONE FUGAZI")
                                .string("TWO FUGAZI")
                                .array(List.of("RED FUGAZI", "BLUE FUGAZI"))
                                .build())));
  }
}
