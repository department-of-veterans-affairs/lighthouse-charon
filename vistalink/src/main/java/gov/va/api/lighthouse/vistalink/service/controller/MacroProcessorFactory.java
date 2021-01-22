package gov.va.api.lighthouse.vistalink.service.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class MacroProcessorFactory {

  List<Macro> macros;

  MacroProcessor create(MacroExecutionContext ctx) {
    return MacroProcessor.builder().macros(macros).macroExecutionContext(ctx).build();
  }
}
