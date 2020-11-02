package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcVistaTargets;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.api.lighthouse.vistalink.service.config.VistalinkProperties;
import gov.va.api.lighthouse.vistalink.service.controller.VistaLinkExceptions.UnknownVista;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class AllVistaNameResolver implements VistaNameResolver {

  @Getter private final VistalinkProperties properties;

  @Override
  public List<ConnectionDetails> resolve(RpcVistaTargets rpcVistaTargets) {
    if (!rpcVistaTargets.include().isEmpty()) {
      var knownVistas = properties.names();
      var unknownVistas =
          Stream.concat(rpcVistaTargets.include().stream(), rpcVistaTargets.exclude().stream())
              .filter(include -> !knownVistas.contains(include))
              .collect(Collectors.toList());
      if (!unknownVistas.isEmpty()) {
        throw new UnknownVista(unknownVistas.toString());
      }
    }

    return properties().vistas();
  }
}
