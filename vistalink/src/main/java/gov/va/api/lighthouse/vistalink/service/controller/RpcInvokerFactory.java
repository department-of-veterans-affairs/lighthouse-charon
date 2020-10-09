package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;

public interface RpcInvokerFactory {
  RpcInvoker create(RpcPrincipal rpcPrincipal, String name);
}
