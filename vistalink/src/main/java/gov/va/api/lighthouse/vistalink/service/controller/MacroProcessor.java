package gov.va.api.lighthouse.vistalink.service.controller;

import lombok.Builder;

@Builder
public class MacroProcessor {

  Macro macro;

  MacroExecutionContext macroExecutionContext;

  String evaluate(String value) {
    if (value.startsWith("${dfn(")) {
      this.macro = new DfnMacro();
    }
    return macro.evaluate(macroExecutionContext, value);
  }
}
