package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import gov.va.api.lighthouse.vistalink.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.models.TypeSafeRpc;
import gov.va.api.lighthouse.vistalink.models.TypeSafeRpcResponse;
import gov.va.api.lighthouse.vistalink.models.XmlResponseRpc;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

public class VprGetPatientData
    extends TypeSafeRpc<VprGetPatientData.Request, VprGetPatientData.Response> {
  private static final String RPC_NAME = "VPR GET PATIENT DATA";

  private static final String RPC_CONTEXT = "VPR APPLICATION PROXY";

  public String rpcContext() {
    return RPC_CONTEXT;
  }

  public String rpcName() {
    return RPC_NAME;
  }

  enum Domains {
    appointments,
    consults,
    demographics,
    documents,
    immunizations,
    labs,
    meds,
    problems,
    procedures,
    reactions,
    visits,
    vitals
  }

  @Data
  @Builder
  static class Response implements TypeSafeRpcResponse {
    List<Results> results;

    public VprGetPatientData.Response fromResult(List<RpcInvocationResult> results) {
      // TO-DO implement with help of XmlResponseRpc utility class
      return Response.builder()
          .results(
              results.stream()
                  .filter(r -> r.error().isEmpty())
                  .map(r -> r.response())
                  .map(r -> XmlResponseRpc.deserialize(r, Results.class))
                  .collect(Collectors.toList()))
          .build();
    }

    @Data
    static class Results {
      List<Vital> vitals;
    }
  }

  @Builder
  static class Request implements TypeSafeRpcRequest {
    String icn;

    Set<Domains> domains;

    Optional<Instant> start;

    Optional<Instant> stop;

    Optional<String> max;

    Optional<String> item;

    List<String> filter;

    public RpcDetails asDetails() {
      return RpcDetails.builder()
          .context(RPC_CONTEXT)
          .name(RPC_NAME)
          .parameters(
              List.of(
                  RpcDetails.Parameter.builder().string(icn).build(),
                  RpcDetails.Parameter.builder()
                      .array(domains.stream().map(Enum::name).collect(Collectors.toList()))
                      .build(),
                  RpcDetails.Parameter.builder().string(start.orElse(null).toString()).build(),
                  RpcDetails.Parameter.builder().string(stop.orElse(null).toString()).build(),
                  RpcDetails.Parameter.builder().string(max.orElse(null)).build(),
                  RpcDetails.Parameter.builder().string(item.orElse(null)).build(),
                  RpcDetails.Parameter.builder().array(filter).build()))
          .build();
    }
  }
}
