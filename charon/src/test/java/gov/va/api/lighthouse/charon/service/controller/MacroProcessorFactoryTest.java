package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MacroProcessorFactoryTest {

  @Mock MacroExecutionContext executionContext;

  @Test
  void macroProcessorIsConfiguredCorrectly() {
    var macroProcessorFactory = new MacroProcessorFactory(FugaziMacros.testMacros());
    MacroProcessor mp =
        macroProcessorFactory.create(executionContext, ConnectionDetails.builder().build());
    assertThat(mp.macros()).containsExactlyInAnyOrderElementsOf(FugaziMacros.testMacros());
    assertThat(mp.macroExecutionContext()).isEqualTo(executionContext);
  }
}
