package gov.va.api.lighthouse.vistalink.service.api;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

public class RpcDetailsTest {
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
