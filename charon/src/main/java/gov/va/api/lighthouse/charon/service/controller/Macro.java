package gov.va.api.lighthouse.charon.service.controller;

public interface Macro {
  String evaluate(MacroExecutionContext ctx, String value);

  String name();
}
