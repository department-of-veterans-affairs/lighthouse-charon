package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse.Status;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
  private final ExecutorService executor =
      Executors.newFixedThreadPool(8, new NamedThreadFactory("rpc-exec"));

  @Override
  public RpcResponse execute(RpcRequest request) {
    var response = RpcResponse.builder();
    List<ConnectionDetails> targets = vistaNameResolver.resolve(request.target());
    if (targets.isEmpty()) {
      return response.status(Status.NO_VISTAS_RESOLVED).build();
    }
    response.status(Status.OK);
    Map<String, Future<RpcInvocationResult>> futureResults = invokeForEachTarget(request, targets);
    targets.stream()
        .map(ConnectionDetails::name)
        .map(vista -> resultOf(vista, futureResults.get(vista)))
        .forEach(
            result -> {
              if (result.error().isPresent()) {
                response.status(Status.FAILED);
              }
              response.result(result);
            });
    return response.build();
  }

  private RpcInvocationResult failed(String vista, String message) {
    return RpcInvocationResult.builder()
        .vista(vista)
        .error(Optional.of("Failed to get result: " + message))
        .build();
  }

  private Map<String, Future<RpcInvocationResult>> invokeForEachTarget(
      RpcRequest request, List<ConnectionDetails> targets) {
    Map<String, Future<RpcInvocationResult>> futures = new HashMap<>(targets.size());
    for (ConnectionDetails target : targets) {
      futures.put(
          target.name(),
          executor.submit(
              () -> safelyInvoke(request, rpcInvokerFactory.create(request.principal(), target))));
    }
    return futures;
  }

  private RpcInvocationResult resultOf(String vista, Future<RpcInvocationResult> futureResult) {
    if (futureResult == null) {
      return failed(vista, "No result found.");
    }
    try {
      return futureResult.get(30, TimeUnit.SECONDS);
    } catch (TimeoutException | ExecutionException | InterruptedException e) {
      log.error("Failed to get result from {}", vista, e);
      return failed(vista, "exception: " + e.getMessage());
    }
  }

  private RpcInvocationResult safelyInvoke(RpcRequest request, RpcInvoker invoker) {
    log.info(
        "Invoking {} / {} for {}", request.rpc().name(), request.rpc().context(), invoker.vista());
    try {
      return invoker.invoke(request.rpc());
    } catch (Exception e) {
      log.error("Error while invoking {} for {}", request.rpc().name(), invoker.vista(), e);
      return failed(invoker.vista(), "exception: " + e.getMessage());
    } finally {
      invoker.close();
    }
  }
}
