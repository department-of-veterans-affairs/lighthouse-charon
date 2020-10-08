package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class ParallelRpcExecutor implements RpcExecutor {

  RpcInvokerFactory rpcInvokerFactory;
  VistaNameResolver vistaNameResolver;

  @Override
  public RpcResponse execute(RpcRequest request) {
    return null;
  }
}
