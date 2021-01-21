package gov.va.api.lighthouse.vistalink.service.controller;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class MacroProcessor {

  Macro macro;

  MacroExecutionContext macroExecutionContext;

  String evaluate(String value) {
    if (value.startsWith("${dfn(") && value.endsWith(")}")) {
      this.macro = new DfnMacro();
    } else {
      return value;
    }
    log.info("Macro found: {}", macro.name());
    return macro.evaluate(macroExecutionContext, value);
  }
}
