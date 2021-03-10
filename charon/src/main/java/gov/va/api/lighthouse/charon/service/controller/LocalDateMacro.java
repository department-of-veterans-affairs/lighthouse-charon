package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.models.FilemanDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

public class LocalDateMacro implements Macro {

  @Override
  public String evaluate(MacroExecutionContext ctx, String value) {
    try {
      return FilemanDate.from(Instant.parse(value))
          .formatAsDateTime(ZoneId.of(ctx.connectionDetails().timezone()));
    } catch (DateTimeParseException e) {
      throw new LocalDateMacroParseFailure();
    }
  }

  @Override
  public String name() {
    return "local-fileman-date";
  }

  static class LocalDateMacroParseFailure extends IllegalArgumentException {}
}
