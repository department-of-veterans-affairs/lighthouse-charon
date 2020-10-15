package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcVistaTargets;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AllVistaNameResolver implements VistaNameResolver {
  // TODO
  @Override
  public List<ConnectionDetails> resolve(RpcVistaTargets rpcVistaTargets) {
    return null;
  }
}
