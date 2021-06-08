package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import gov.va.api.lighthouse.charon.models.TypeSafeRpc;
import gov.va.api.lighthouse.charon.models.TypeSafeRpcRequest;
import gov.va.api.lighthouse.charon.models.TypeSafeRpcResponse;
import lombok.Builder;
import lombok.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class IblhsAmcmsGetIns
    implements TypeSafeRpc<IblhsAmcmsGetIns.Request, IblhsAmcmsGetIns.Response> {

  private static final String RPC_NAME = "IBLHS AMCMS GET INS";

  private static final String RPC_CONTEXT = "IBLHS AMCMS RPCS";

  @Override
  public IblhsAmcmsGetIns.Response fromResults(List<RpcInvocationResult> results) {
    return IblhsAmcmsGetIns.Response.builder()
        .resultsByStation(
            results.stream()
                .filter(result -> result.error().isEmpty())
                .collect(toMap(RpcInvocationResult::vista, RpcInvocationResult::response)))
        .build();
  }

  /** Java representation of the RPC request. */
  @Builder
  public static class Request implements TypeSafeRpcRequest {

    @NonNull private String icn;

    @Override
    public RpcDetails asDetails() {
      return RpcDetails.builder()
          .name(RPC_NAME)
          .context(RPC_CONTEXT)
          .parameters(List.of(RpcDetails.Parameter.builder().string(icn).build()))
          .build();
    }
  }

  /** Java representation of the RPC response per VistA site. */
  @Builder
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
