package gov.va.api.lighthouse.charon.service.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Value
@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class MacroProcessorFactory {

  @NonNull List<Macro> macros;

  public MacroProcessor create(MacroExecutionContext ctx) {
    return MacroProcessor.builder().macros(macros).macroExecutionContext(ctx).build();
  }
}
