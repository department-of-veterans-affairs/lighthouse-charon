package gov.va.api.lighthouse.vistalink.service.controller;

public class ToUpperCaseMacro implements Macro {
  @Override
  public String evaluate(MacroExecutionContext ctx, String value) {
    return value.toUpperCase();
  }

  @Override
  public String name() {
    return "touppercase";
  }
}
