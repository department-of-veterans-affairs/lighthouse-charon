package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

public class MacroProcessorFactoryTest {
  @Test
  void buildMacroProcessorFactory() {
    var macroProcessorFactory = new MacroProcessorFactory(List.of(new DfnMacro()));
    assertThat(macroProcessorFactory).isInstanceOf(MacroProcessorFactory.class);
  }
}
