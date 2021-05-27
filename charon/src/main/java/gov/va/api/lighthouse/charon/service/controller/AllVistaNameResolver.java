package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.config.VistalinkProperties;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** Name resolver for use with all known vista sites. */
@Component
@ConditionalOnProperty(name = "vistalink.resolver", havingValue = "all")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class AllVistaNameResolver implements VistaNameResolver {

  @Getter private final VistalinkProperties properties;

  private Collection<String> allTargets(RpcVistaTargets rpcVistaTargets) {
    if (rpcVistaTargets.include().isEmpty()) {
      return properties().names();
    }
    return rpcVistaTargets.include();
  }

  @Override
  public List<ConnectionDetails> resolve(RpcVistaTargets rpcVistaTargets) {
    return NameResolution.builder()
        .properties(properties())
        .additionalCandidates(this::allTargets)
        .build()
        .resolve(rpcVistaTargets);
  }
}
