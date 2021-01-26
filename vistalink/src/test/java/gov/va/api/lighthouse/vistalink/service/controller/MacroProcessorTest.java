package gov.va.api.lighthouse.vistalink.service.controller;

import static gov.va.api.lighthouse.vistalink.api.RpcDetails.Parameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import java.util.Map;
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
    List<String> stringList = List.of("${appendx(456)}", "zxc", "${touppercase(def)}");
    List<String> expectedList = List.of("456x", "zxc", "DEF");
    Map<String, String> stringMap =
        Map.of("upper", "${touppercase(iop)}", "append", "${appendx(789)}", "none", "jkl");
    Map<String, String> expectedMap = Map.of("upper", "IOP", "append", "789x", "none", "jkl");
    assertThat(macroProcessor.evaluate("${appendx(123)}")).isEqualTo("123x");
    assertThat(macroProcessor.evaluate("${touppercase(abc)}")).isEqualTo("ABC");
    assertThat(macroProcessor.evaluate("${touppercase()}")).isEqualTo("");
    assertThat(macroProcessor.evaluate(stringList))
        .containsExactlyInAnyOrderElementsOf(expectedList);
    assertThat(macroProcessor.evaluate(stringMap)).containsExactlyInAnyOrderEntriesOf(expectedMap);

    assertThat(macroProcessor.evaluate(Parameter.builder().string("${appendx(bnm)}").build()))
        .isEqualTo("bnmx");
    assertThat(macroProcessor.evaluate(Parameter.builder().ref("${touppercase(bnm)}").build()))
        .isEqualTo("BNM");
    assertThat(macroProcessor.evaluate(Parameter.builder().array(stringList).build()))
        .isEqualTo(expectedList);
    assertThat(macroProcessor.evaluate(Parameter.builder().namedArray(stringMap).build()))
        .isEqualTo(expectedMap);
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
    assertThat(macroProcessor.evaluate(List.of("notAMacro", "${touppercase}")))
        .containsExactlyInAnyOrderElementsOf(List.of("${touppercase}", "notAMacro"));
    assertThat(macroProcessor.evaluate(Map.of(" ", "{touppercase(abc)}")))
        .containsExactlyInAnyOrderEntriesOf(Map.of(" ", "{touppercase(abc)}"));
  }

  private static class BoomBoom extends RuntimeException {}
}
