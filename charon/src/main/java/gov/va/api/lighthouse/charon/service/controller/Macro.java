package gov.va.api.lighthouse.charon.service.controller;

/** Interface for defining Macros. Macro's are used to pre-process requests. */
public interface Macro {
  String evaluate(MacroExecutionContext ctx, String value);

  String name();
}
