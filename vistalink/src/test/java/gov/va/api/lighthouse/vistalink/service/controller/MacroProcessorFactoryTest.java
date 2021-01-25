package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MacroProcessorFactoryTest {

  @Mock MacroExecutionContext executionContext;

  @Test
  void buildMacroProcessorFactory() {
    var macroProcessorFactory =
        new MacroProcessorFactory(List.of(new AppendXMacro(), new ToUpperCaseMacro()));
    assertThat(macroProcessorFactory).isInstanceOf(MacroProcessorFactory.class);
    assertThat(macroProcessorFactory.create(executionContext)).isInstanceOf(MacroProcessor.class);
  }
}
