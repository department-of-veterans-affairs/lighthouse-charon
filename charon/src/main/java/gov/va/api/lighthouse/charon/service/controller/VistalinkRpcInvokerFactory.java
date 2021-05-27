package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The VistalinkRpcInvokerFactory creates RpcInvokers that know how to use vistalink to invoke rpc
 * requests.
 */
@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class VistalinkRpcInvokerFactory implements RpcInvokerFactory {

  private final MacroProcessorFactory macroProcessorFactory;

  @Override
  public RpcInvoker create(RpcPrincipal rpcPrincipal, ConnectionDetails connectionDetails) {
    return VistalinkRpcInvoker.builder()
        .rpcPrincipal(rpcPrincipal)
        .connectionDetails(connectionDetails)
        .macroProcessorFactory(macroProcessorFactory)
        .build();
  }
}
