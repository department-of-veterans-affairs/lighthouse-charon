package gov.va.api.lighthouse.charon.models.lhslighthouserpcgateway;

import static java.lang.String.join;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import gov.va.api.lighthouse.charon.models.TypeSafeRpc;
import gov.va.api.lighthouse.charon.models.TypeSafeRpcRequest;
import gov.va.api.lighthouse.charon.models.TypeSafeRpcResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

/** Java representation of the LHS LIGHTHOUSE RPC GATEWAY RPC GETS manifest. */
@NoArgsConstructor(staticName = "create")
public class LhsLighthouseRpcGatewayGetsManifest
    implements TypeSafeRpc<
        LhsLighthouseRpcGatewayGetsManifest.Request, LhsLighthouseRpcGatewayGetsManifest.Response> {
  public static final String RPC_NAME = "LHS LIGHTHOUSE RPC GATEWAY";

  private static final String DEFAULT_RPC_CONTEXT = "LHS RPC CONTEXT";

  @SneakyThrows
  private LighthouseRpcGatewayResults deserialize(String value) {
    return JacksonConfig.createMapper().readValue(value, LighthouseRpcGatewayResults.class);
  }

  @Override
  @SneakyThrows
  public LhsLighthouseRpcGatewayGetsManifest.Response fromResults(
      List<RpcInvocationResult> results) {
    return LhsLighthouseRpcGatewayGetsManifest.Response.builder()
        .resultsByStation(
            results.stream()
                .filter(invocationResult -> invocationResult.error().isEmpty())
                .collect(toMap(r -> r.vista(), r -> deserialize(r.response()))))
        .build();
  }

  /** Build an RPC Request using field names. */
  @Data
  @Builder
  public static class Request implements TypeSafeRpcRequest {
    @Builder.Default String debugMode = "1";

    @NonNull String file;

    @NonNull String iens;

    @NonNull List<String> fields;

    List<GetsManifestFlags> flags;

    @Override
    public RpcDetails asDetails() {
      List<String> parameters = new ArrayList<>(6);
      parameters.add("debugmode^" + debugMode());
      parameters.add("api^manifest^gets");
      parameters.add("param^FILE^literal^" + file());
      parameters.add("param^IENS^literal^" + iens());
      parameters.add("param^FIELDS^literal^" + join(";", fields()));
      parameters.add(
          "param^FLAGS^literal^"
              + flags().stream().map(GetsManifestFlags::flag).collect(joining("")));
      return RpcDetails.builder()
          .name(RPC_NAME)
          .context(DEFAULT_RPC_CONTEXT)
          .parameters(List.of(RpcDetails.Parameter.builder().array(parameters).build()))
          .build();
    }

    /** Lazy Initializer. */
    List<GetsManifestFlags> flags() {
      if (flags == null) {
        flags = List.of();
      }
      return flags;
    }

    @AllArgsConstructor
    public enum GetsManifestFlags {
      RETURN_EXTERNAL_VALUES("E"),
      RETURN_INTERNAL_VALUES("I"),
      DONT_RETURN_NULL("N"),
      USE_FIELD_NAMES("R"),
      INCLUDE_ZERO_NODES("Z"),
      USE_AUDIT_TRAIL("A#");

      @Getter private final String flag;
    }
  }

  /** Java model of the RPC's string response for (de)serialization. */
  @Data
  @Builder
  public static class Response implements TypeSafeRpcResponse {
    private Map<String, LighthouseRpcGatewayResults> resultsByStation;

    /** Lazy Initialization. */
    Map<String, LighthouseRpcGatewayResults> resultsByStation() {
      if (resultsByStation == null) {
        resultsByStation = Map.of();
      }
      return resultsByStation;
    }
  }
}
