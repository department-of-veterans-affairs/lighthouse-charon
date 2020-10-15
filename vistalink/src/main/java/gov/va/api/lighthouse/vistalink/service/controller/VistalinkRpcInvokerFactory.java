package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.config.VistalinkProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class VistalinkRpcInvokerFactory implements RpcInvokerFactory {

  private final VistalinkProperties vistalinkProperties;

  @Override
  public RpcInvoker create(RpcPrincipal rpcPrincipal, String name) {

    return VistalinkRpcInvoker.builder()
        .rpcPrincipal(rpcPrincipal)
        //TODO: which connection details to i take? is it name == host?
        .connectionDetails(vistalinkProperties.getVistas().get(0))
        .build();
  }
}
