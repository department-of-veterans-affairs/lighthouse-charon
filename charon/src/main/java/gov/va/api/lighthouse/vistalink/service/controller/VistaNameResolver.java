package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.api.RpcVistaTargets;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import java.util.List;

public interface VistaNameResolver {
  List<ConnectionDetails> resolve(RpcVistaTargets rpcVistaTargets);
}
