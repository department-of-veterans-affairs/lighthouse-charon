package gov.va.api.lighthouse.vistalink.service.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor(onConstructor_ = @Autowired)
public class MacroProcessorFactory {

  Macro macro;

  MacroProcessor create(MacroExecutionContext ctx) {
    return MacroProcessor.builder().macro(macro).macroExecutionContext(ctx).build();
  }
}
