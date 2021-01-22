package gov.va.api.lighthouse.vistalink.service.controller;

import java.util.List;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class MacroProcessor {

  List<Macro> macros;

  MacroExecutionContext macroExecutionContext;

  String evaluate(String value) {
    for (Macro macro : macros) {
      if (value.startsWith("${" + macro.name() + "(") && value.endsWith(")}")) {
        log.info("Macro found: {}", macro.name());
        return macro.evaluate(
            macroExecutionContext, value.substring(macro.name().length() + 3, value.length() - 2));
      }
    }
    return value;
  }
}
