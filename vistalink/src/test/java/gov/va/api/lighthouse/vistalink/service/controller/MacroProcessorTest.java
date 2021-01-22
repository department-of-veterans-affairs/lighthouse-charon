package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

public class MacroProcessorTest {

  @Test
  void buildMacroProcessorTest() {
    var macroProcessor = MacroProcessor.builder().macros(List.of(new DfnMacro())).build();
    assertThat(macroProcessor).isInstanceOf(MacroProcessor.class);
    assertThat(macroProcessor.evaluate("${dfn(123)}")).isEqualTo("todoDfnMacro");
  }
}
