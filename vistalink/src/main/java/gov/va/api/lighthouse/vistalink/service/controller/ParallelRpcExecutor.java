package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse.Status;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.api.lighthouse.vistalink.service.controller.UnrecoverableVistalinkExceptions.UnrecoverableVistalinkException;
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
import javax.security.auth.login.LoginException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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

  @SneakyThrows
  private RpcInvocationResult failed(String vista, String message) {
    return RpcInvocationResult.builder()
        .vista(vista)
        .error(Optional.of("Failed to get result: " + message))
        .build();
  }

  @SneakyThrows
  private RpcInvocationResult handleExecutionException(String vista, ExecutionException exception) {
    var cause = exception.getCause();
    log.error("Call failed. Cause: {}", cause, exception);
    if (cause instanceof LoginException) {
      throw cause;
    }
    return failed(vista, "exception: " + exception.getMessage());
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

  @SneakyThrows
  private RpcInvocationResult resultOf(String vista, Future<RpcInvocationResult> futureResult) {
    if (futureResult == null) {
      return failed(vista, "No result found.");
    }
    try {
      return futureResult.get(30, TimeUnit.SECONDS);
    } catch (ExecutionException e) {
      /* Conditionally rethrow the ExecutionException cause. */
      return handleExecutionException(vista, e);
    } catch (TimeoutException e) {
      /*
       * Rethrow critical errors and let application exception handling produce the appropriate
       * response.
       */
      log.error("Request failed: ", e);
      throw e;
    } catch (InterruptedException e) {
      /* Suppress exception and return a failed response. */
      log.error("Failed to get result from {}", vista, e);
      return failed(vista, "exception: " + e.getMessage());
    }
  }

  @SneakyThrows
  private RpcInvocationResult safelyInvoke(RpcRequest request, RpcInvoker invoker) {
    log.info(
        "Invoking {} / {} for {}", request.rpc().name(), request.rpc().context(), invoker.vista());
    try {
      return invoker.invoke(request.rpc());
    } catch (UnrecoverableVistalinkException e) {
      log.error(
          "Unrecoverable error while invoking {} for {}", request.rpc().name(), invoker.vista(), e);
      throw e;
    } catch (Exception e) {
      log.error("Error while invoking {} for {}", request.rpc().name(), invoker.vista(), e);
      return failed(invoker.vista(), e.getClass().getSimpleName() + ": " + e.getMessage());
    } finally {
      invoker.close();
    }
  }
}
