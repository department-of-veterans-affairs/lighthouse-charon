package gov.va.api.lighthouse.charon.models.lhslighthouserpcgateway;

import static java.lang.String.join;
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
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Value;

/** Java representation of the LHS LIGHTHOUSE RPC GATEWAY RPC. */
@NoArgsConstructor(staticName = "create")
public class LhsLighthouseRpcGateway
    implements TypeSafeRpc<LhsLighthouseRpcGateway.Request, LhsLighthouseRpcGateway.Response> {
  public static final String RPC_NAME = "LHS LIGHTHOUSE RPC GATEWAY";

  private static final String DEFAULT_RPC_CONTEXT = "LHS RPC CONTEXT";

  @SneakyThrows
  private LhsLighthouseRpcGateway.Response.Results deserialize(String value) {
    return JacksonConfig.createMapper()
        .readValue(value, LhsLighthouseRpcGateway.Response.Results.class);
  }

  @Override
  @SneakyThrows
  public TypeSafeRpcResponse fromResults(List<RpcInvocationResult> results) {
    return LhsLighthouseRpcGateway.Response.builder()
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

    @NonNull ApiManifest api;

    @NonNull String file;

    List<String> iens;

    List<String> fields;

    List<String> flags;

    Optional<String> number;

    Optional<From> from;

    Optional<String> part;

    Optional<String> index;

    Optional<String> screen;

    Optional<String> id;

    @Override
    public RpcDetails asDetails() {
      List<String> parameters = new ArrayList<>(6);
      parameters.add("debugmode^" + debugMode());
      parameters.add(api().rpcParamString());
      parameters.add("param^FILE^literal^" + file());
      parameters.add("param^IENS^literal^" + join(";", iens()));
      parameters.add("param^FIELDS^literal^" + join(";", fields()));
      parameters.add("param^FLAGS^literal^" + join("", flags()));
      parameters.add("param^NUMBER^literal^" + number().orElse(""));
      if (from().isPresent()) {
        parameters.add("param^FROM^list^1^" + from().get().name());
        parameters.add("param^FROM^list^2^" + from().get().ien());
        parameters.add("param^FROM^list^IEN^" + from().get().ien());
      } else {
        parameters.add("param^FROM^literal^");
      }
      if (part().isPresent()) {
        parameters.add("param^PART^list^1^" + part().get());
      } else {
        parameters.add("param^PART^literal^");
      }
      parameters.add("param^INDEX^literal^" + index().orElse(""));
      parameters.add("param^SCREEN^literal^" + screen().orElse(""));
      parameters.add("param^ID^literal^" + id().orElse(""));
      return RpcDetails.builder()
          .name(RPC_NAME)
          .context(DEFAULT_RPC_CONTEXT)
          .parameters(List.of(RpcDetails.Parameter.builder().array(parameters).build()))
          .build();
    }

    /** Lazy Initializer. */
    List<String> fields() {
      if (fields == null) {
        fields = List.of();
      }
      return fields;
    }

    /** Lazy Initializer. */
    List<String> flags() {
      if (flags == null) {
        flags = List.of();
      }
      return flags;
    }

    /** Lazy Initializer. */
    Optional<From> from() {
      if (from == null) {
        from = Optional.empty();
      }
      return from;
    }

    /** Lazy Initializer. */
    Optional<String> id() {
      if (id == null) {
        id = Optional.empty();
      }
      return id;
    }

    /** Lazy Initializer. */
    List<String> iens() {
      if (iens == null) {
        iens = List.of();
      }
      return iens;
    }

    /** Lazy Initializer. */
    Optional<String> index() {
      if (index == null) {
        index = Optional.empty();
      }
      return index;
    }

    /** Lazy Initializer. */
    Optional<String> number() {
      if (number == null) {
        number = Optional.empty();
      }
      return number;
    }

    /** Lazy Initializer. */
    Optional<String> part() {
      if (part == null) {
        part = Optional.empty();
      }
      return part;
    }

    /** Lazy Initializer. */
    Optional<String> screen() {
      if (screen == null) {
        screen = Optional.empty();
      }
      return screen;
    }

    @AllArgsConstructor
    public enum ApiManifest {
      GETS("api^manifest^gets"),
      LIST("api^manifest^list");

      @Getter private final String rpcParamString;
    }

    @Value
    @Builder
    public static class From {
      String name;

      String ien;
    }
  }

  /** Java model of the RPC's string response for (de)serialization. */
  @Data
  @Builder
  public static class Response implements TypeSafeRpcResponse {
    private Map<String, Results> resultsByStation;

    /** Lazy Initialization. */
    Map<String, Results> resultsByStation() {
      if (resultsByStation == null) {
        resultsByStation = Map.of();
      }
      return resultsByStation;
    }

    @Value
    @Builder
    public static class Results {
      private List<FilemanEntry> results;
    }

    @Value
    @Builder
    public static class FilemanEntry {
      private Map<String, Values> fields;

      private String file;

      private String ien;
    }

    @Value
    @Builder
    @AllArgsConstructor(staticName = "of")
    public static class Values {
      String ext;

      String in;
    }
  }
}
