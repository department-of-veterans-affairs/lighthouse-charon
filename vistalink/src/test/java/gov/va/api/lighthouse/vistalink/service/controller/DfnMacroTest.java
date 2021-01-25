package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DfnMacroTest {

  @Mock MacroExecutionContext executionContext;

  @Test
  void checkDfnMacro() {
    var dfn = new DfnMacro();
    assertThat(dfn.name()).isEqualTo("dfn"); //Coverage requirement, will remove
    assertThat(dfn.evaluate(executionContext, "5000000347")).isEqualTo("5000000347");
  }
}
