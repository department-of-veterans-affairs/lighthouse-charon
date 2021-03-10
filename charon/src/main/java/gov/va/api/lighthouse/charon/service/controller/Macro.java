package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;

public interface Macro {
  String evaluate(MacroExecutionContext ctx, ConnectionDetails details, String value);

  String name();
}
