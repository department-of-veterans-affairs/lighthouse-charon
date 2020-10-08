package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ParallelRpcExecutor implements RpcExecutor {

  private final RpcInvokerFactory rpcInvokerFactory;
  private final VistaNameResolver vistaNameResolver;

  @Override
  public RpcResponse execute(RpcRequest request) {
    log.info("{},{}", rpcInvokerFactory, vistaNameResolver);
    return null;
  }
}
