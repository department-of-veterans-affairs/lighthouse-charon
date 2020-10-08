package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.config.VistalinkProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VistalinkRpcInvokerFactory implements RpcInvokerFactory {

  @Autowired VistalinkProperties vistalinkProperties;

  // TODO
  @Override
  public RpcInvoker create(RpcPrincipal rpcPrincipal, String name) {
    return null;
  }
}
