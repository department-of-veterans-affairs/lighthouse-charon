package gov.va.api.lighthouse.vistalink.service.controller;

import java.util.List;
import lombok.EqualsAndHashCode;

class FugaziMacros {

  static List<Macro> testMacros() {
    return List.of(new AppendXMacro(), new ToUpperCaseMacro());
  }

  @EqualsAndHashCode
  public static class AppendXMacro implements Macro {
    @Override
    public String evaluate(MacroExecutionContext ctx, String value) {
      return value + "x";
    }

    @Override
    public String name() {
      return "appendx";
    }
  }

  @EqualsAndHashCode
  public static class ToUpperCaseMacro implements Macro {
    @Override
    public String evaluate(MacroExecutionContext ctx, String value) {
      return value.toUpperCase();
    }

    @Override
    public String name() {
      return "touppercase";
    }
  }
}
