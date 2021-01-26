package gov.va.api.lighthouse.vistalink.service.controller;

import static java.util.stream.Collectors.toList;

import gov.va.api.lighthouse.vistalink.api.RpcDetails.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
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

  public List<String> evaluate(List<String> value) {
    return value.stream().map(this::evaluate).collect(toList());
  }

  public Map<String, String> evaluate(Map<String, String> value) {
    return value.entrySet().stream()
        .collect(Collectors.toMap(Entry::getKey, e -> evaluate(e.getValue())));
  }

  public Object evaluate(Parameter parameter) {
    if (parameter == null) {
      return null;
    }
    if (parameter.isString()) {
      return evaluate(parameter.string());
    }
    if (parameter.isRef()) {
      return evaluate(parameter.ref());
    }
    if (parameter.isArray()) {
      return evaluate(parameter.array());
    }
    if (parameter.isNamedArray()) {
      return evaluate(parameter.namedArray());
    }
    throw new IllegalArgumentException("Cannot evaluate type: " + parameter.type());
  }
}
