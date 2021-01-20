package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class MacroProcessorFactoryTest {
  @Test
  void buildMacroProcessorFactory() {
    var macroProcessorFactory = new MacroProcessorFactory(new DfnMacro());
    assertThat(macroProcessorFactory).isInstanceOf(MacroProcessorFactory.class);
    assertThat(macroProcessorFactory.create(new ExampleMacroExecutionContext()))
        .isInstanceOf(MacroProcessor.class);
  }
}
