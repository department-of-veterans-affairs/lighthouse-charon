package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.config.VistalinkProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class VistalinkRpcInvokerFactory implements RpcInvokerFactory {

  private final VistalinkProperties vistalinkProperties;

  // TODO
  @Override
  public RpcInvoker create(RpcPrincipal rpcPrincipal, String name) {
    log.info("{}", vistalinkProperties);
    return null;
  }
}
