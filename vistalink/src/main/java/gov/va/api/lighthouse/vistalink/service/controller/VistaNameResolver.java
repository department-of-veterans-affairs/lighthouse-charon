package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcVistaTargets;
import java.util.List;

public interface VistaNameResolver {

  List<String> resolve(RpcVistaTargets rpcVistaTargets);
}
