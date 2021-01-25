package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MacroProcessorTest {

  @Mock MacroExecutionContext executionContext;

  @Test
  void checkMacroProcessor() {
    var macroProcessor = macroProcessor();
    assertThat(macroProcessor.evaluate("${appendx(123)}")).isEqualTo("123x");
    assertThat(macroProcessor.evaluate("${touppercase(abc)}")).isEqualTo("ABC");
    assertThat(macroProcessor.evaluate("${touppercase()}")).isEqualTo("");
  }

  @Test
  void failedEvaluationPropagatesExceptions() {
    var macroProcessor =
        MacroProcessor.builder()
            .macros(
                List.of(
                    new Macro() {
                      @Override
                      public String evaluate(MacroExecutionContext ctx, String value) {
                        throw new BoomBoom();
                      }

                      @Override
                      public String name() {
                        return "boom";
                      }
                    }))
            .macroExecutionContext(executionContext)
            .build();
    assertThatExceptionOfType(BoomBoom.class)
        .isThrownBy(() -> macroProcessor.evaluate("${boom()}"));
  }

  private MacroProcessor macroProcessor() {
    return MacroProcessor.builder()
        .macros(FugaziMacros.testMacros())
        .macroExecutionContext(executionContext)
        .build();
  }

  @Test
  void notAMacro() {
    var macroProcessor = macroProcessor();
    assertThat(macroProcessor.evaluate("notAMacro")).isEqualTo("notAMacro");
    assertThat(macroProcessor.evaluate("${notAMacro(null)}")).isEqualTo("${notAMacro(null)}");
    assertThat(macroProcessor.evaluate("${touppercase}")).isEqualTo("${touppercase}");
  }

  private static class BoomBoom extends RuntimeException {}
}
