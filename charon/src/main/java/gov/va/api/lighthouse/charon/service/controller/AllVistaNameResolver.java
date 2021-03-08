package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.config.VistalinkProperties;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "vistalink.resolver", havingValue = "all")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class AllVistaNameResolver implements VistaNameResolver {

  @Getter private final VistalinkProperties properties;

  @Override
  public List<ConnectionDetails> resolve(RpcVistaTargets rpcVistaTargets) {
    properties.checkKnownNames(rpcVistaTargets.include());
    properties.checkKnownNames(rpcVistaTargets.exclude());
    Predicate<ConnectionDetails> allowed;
    if (rpcVistaTargets.include().isEmpty()) {
      allowed = s -> true;
    } else {
      allowed = s -> rpcVistaTargets.include().contains(s.name());
    }
    if (!rpcVistaTargets.exclude().isEmpty()) {
      allowed = allowed.and(s -> !rpcVistaTargets.exclude().contains(s.name()));
    }
    return properties().vistas().stream().filter(allowed).collect(Collectors.toList());
  }
}
