package gov.va.api.lighthouse.vistalink.service.controller;

public class AppendXMacro implements Macro {
  @Override
  public String evaluate(MacroExecutionContext ctx, String value) {
    return value + "x";
  }

  @Override
  public String name() {
    return "appendx";
  }
}
