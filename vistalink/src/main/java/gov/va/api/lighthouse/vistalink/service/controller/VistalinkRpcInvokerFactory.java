package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class VistalinkRpcInvokerFactory implements RpcInvokerFactory {
  @Override
  public RpcInvoker create(RpcPrincipal rpcPrincipal, ConnectionDetails connectionDetails) {
    return VistalinkRpcInvoker.builder()
        .rpcPrincipal(rpcPrincipal)
        .connectionDetails(connectionDetails)
        .build();
  }
}
