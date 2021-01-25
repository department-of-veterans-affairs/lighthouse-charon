package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

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
    var macroProcessor =
        MacroProcessor.builder()
            .macros(List.of(new AppendXMacro(), new ToUpperCaseMacro()))
            .macroExecutionContext(executionContext)
            .build();
    assertThat(macroProcessor.evaluate("${appendx(123)}")).isEqualTo("123x");
    assertThat(macroProcessor.evaluate("${touppercase(abc)}")).isEqualTo("ABC");
  }

  @Test
  void notAMacro() {
    var macroProcessor =
        MacroProcessor.builder()
            .macros(List.of(new AppendXMacro(), new ToUpperCaseMacro()))
            .macroExecutionContext(executionContext)
            .build();
    assertThat(macroProcessor.evaluate("notAMacro")).isEqualTo("notAMacro");
    assertThat(macroProcessor.evaluate("${notAMacro(null)}")).isEqualTo("${notAMacro(null)}");
  }
}
