package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;

public interface RpcInvokerFactory {
  RpcInvoker create(RpcPrincipal rpcPrincipal, ConnectionDetails details);
}
