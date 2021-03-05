package gov.va.api.lighthouse.vistalink.service.controller;

public interface Macro {
  String evaluate(MacroExecutionContext ctx, String value);

  String name();
}
