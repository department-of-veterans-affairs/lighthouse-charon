package gov.va.api.lighthouse.charon.models.lhscheckoptionaccess;

import static java.util.stream.Collectors.toMap;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import gov.va.api.lighthouse.charon.models.TypeSafeRpc;
import gov.va.api.lighthouse.charon.models.TypeSafeRpcRequest;
import gov.va.api.lighthouse.charon.models.TypeSafeRpcResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "create")
public class LhsCheckOptionAccess
    implements TypeSafeRpc<LhsCheckOptionAccess.Request, LhsCheckOptionAccess.Response> {

  private static final String RPC_NAME = "LHS CHECK OPTION ACCESS";
  private static final String RPC_CONTEXT = "LHS RPC CONTEXT";

  @Override
  public LhsCheckOptionAccess.Response fromResults(List<RpcInvocationResult> results) {
    return Response.builder()
        .resultsByStation(
            results.stream()
                .filter(result -> result.error().isEmpty())
                .collect(toMap(RpcInvocationResult::vista, RpcInvocationResult::response)))
        .build();
  }

  @Builder
  public static class Request implements TypeSafeRpcRequest {
    String duz;
    String menuOption;
    Optional<String> context;

    @Override
    public RpcDetails asDetails() {
      return RpcDetails.builder()
          .context(context().orElse(RPC_CONTEXT))
          .name(RPC_NAME)
          .parameters(
              List.of(
                  RpcDetails.Parameter.builder().string(duz).build(),
                  RpcDetails.Parameter.builder().string(menuOption).build()))
          .build();
    }

    /** Lazy Initializer. */
    Optional<String> context() {
      if (context == null) {
        context = Optional.empty();
      }
      return context;
    }
  }

  @Builder
  @Data
  public static class Response implements TypeSafeRpcResponse {
    private Map<String, String> resultsByStation;
  }
}
