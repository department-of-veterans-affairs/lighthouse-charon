package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class MacroProcessorTest {

  @Test
  void buildMacroProcessorTest() {
    var macroProcessor =
        MacroProcessor.builder()
            .macro(new DfnMacro())
            .macroExecutionContext(new ExampleMacroExecutionContext())
            .build();
    assertThat(macroProcessor).isInstanceOf(MacroProcessor.class);
    assertThat(macroProcessor.evaluate("${dfn(123)}")).isEqualTo("todoDfnMacro");
  }
}
