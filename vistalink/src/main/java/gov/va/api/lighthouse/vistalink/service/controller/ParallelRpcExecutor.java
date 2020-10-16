package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse.Status;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import java.util.List;
import java.util.Optional;
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
    var response = RpcResponse.builder();
    List<ConnectionDetails> targets = vistaNameResolver.resolve(request.target());
    if (targets.isEmpty()) {
      return response.status(Status.NO_VISTAS_RESOLVED).build();
    }
    response.status(Status.OK);
    targets.parallelStream()
        .map(details -> rpcInvokerFactory.create(request.principal(), details))
        .map(invoker -> safelyInvoke(request, invoker))
        .forEachOrdered(
            result -> {
              if (result.error().isPresent()) {
                response.status(Status.FAILED);
              }
              response.result(result);
            });
    return response.build();
  }

  private RpcInvocationResult safelyInvoke(RpcRequest request, RpcInvoker invoker) {
    try {
      return invoker.invoke(request.rpc());
    } catch (Exception e) {
      log.error("Error while invoking {} for {}", request.rpc().name(), invoker.vista(), e);
      return RpcInvocationResult.builder()
          .vista(invoker.vista())
          .error(Optional.of("exception: " + e.getMessage()))
          .build();
    }
  }
}
