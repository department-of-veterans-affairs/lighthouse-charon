package gov.va.api.lighthouse.charon.models.lhscheckoptionaccess;

import static java.util.stream.Collectors.toMap;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import gov.va.api.lighthouse.charon.models.TypeSafeRpc;
import gov.va.api.lighthouse.charon.models.TypeSafeRpcRequest;
import gov.va.api.lighthouse.charon.models.TypeSafeRpcResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Helper class for invoking the LhsCheckOptionAccess rpc. */
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

  /** Type safe Request class for invoking LhsCheckOptionAccess rpc. */
  @Builder
  public static class Request implements TypeSafeRpcRequest {
    private String duz;
    private String menuOption;
    private Optional<String> context;

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
    private Optional<String> context() {
      if (context == null) {
        context = Optional.empty();
      }
      return context;
    }
  }

  /** Type safe Response class for invoking LhsCheckOptionAccess rpc. */
  @Builder
  @Data
  public static class Response implements TypeSafeRpcResponse {
    private Map<String, String> resultsByStation;

    Map<String, String> getResultsByStation() {
      if (resultsByStation == null) {
        resultsByStation = new HashMap<>();
      }
      return resultsByStation;
    }
  }
}
