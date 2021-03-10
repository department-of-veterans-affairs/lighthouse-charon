package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LocalDateMacroTest {
  @Mock MacroExecutionContext ctx;

  ConnectionDetails connectionDetails =
      ConnectionDetails.builder().timezone("America/Chicago").build();

  @Test
  void evaluateParsesGoodDates() {
    assertThat(new LocalDateMacro().evaluate(ctx, connectionDetails, "2021-02-22T13:28:00Z"))
        .isEqualTo("3210222.0728");
  }

  @Test
  void evaluateThrowsDateTimeParseException() {
    assertThatExceptionOfType(LocalDateMacro.LocalDateMacroParseFailure.class)
        .isThrownBy(() -> new LocalDateMacro().evaluate(ctx, connectionDetails, "123"));
  }
}
