package gov.va.api.lighthouse.vistalink.service.controller;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Value
@Slf4j
@Builder
public class MacroProcessor {

  @NonNull List<Macro> macros;

  @NonNull MacroExecutionContext macroExecutionContext;

  /** Iterates over macro list, applies the one that is found in the syntax. */
  public String evaluate(String value) {
    for (Macro macro : macros) {
      if (value.startsWith("${" + macro.name() + "(") && value.endsWith(")}")) {
        try {
          var subst =
              macro.evaluate(
                  macroExecutionContext,
                  value.substring(macro.name().length() + 3, value.length() - 2));
          log.info("Macro {}: {} = {}", macro.name(), value, subst);
          return subst;
        } catch (Exception e) {
          log.error("Macro {}: {} failed.", macro.name(), value, e);
          throw e;
        }
      }
    }
    return value;
  }
}
