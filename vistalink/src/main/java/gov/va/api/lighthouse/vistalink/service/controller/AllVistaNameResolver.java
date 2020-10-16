package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcVistaTargets;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.api.lighthouse.vistalink.service.config.VistalinkProperties;
import java.util.List;
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
    return properties().vistas();
  }
}
