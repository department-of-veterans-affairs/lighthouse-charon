package gov.va.api.lighthouse.charon.service.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class MacroProcessorFactory {

  private final @NonNull List<Macro> macros;

  public MacroProcessor create(MacroExecutionContext ctx) {
    return MacroProcessor.builder().macros(macros).macroExecutionContext(ctx).build();
  }
}
