package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParallelRpcExecutor implements RpcExecutor {

  @Autowired RpcInvokerFactory rpcInvokerFactory;
  @Autowired VistaNameResolver vistaNameResolver;

  @Override
  public RpcResponse execute(RpcRequest request) {
    return null;
  }
}
