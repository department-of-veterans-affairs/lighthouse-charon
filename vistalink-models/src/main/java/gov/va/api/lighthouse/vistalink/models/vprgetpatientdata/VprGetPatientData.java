package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import gov.va.api.lighthouse.vistalink.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.models.TypeSafeRpc;
import gov.va.api.lighthouse.vistalink.models.TypeSafeRpcResponse;
import gov.va.api.lighthouse.vistalink.models.XmlResponseRpc;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

public class VprGetPatientData
    implements TypeSafeRpc<VprGetPatientData.Request, VprGetPatientData.Response> {
  private static final String RPC_NAME = "VPR GET PATIENT DATA";

  private static final String RPC_CONTEXT = "VPR APPLICATION PROXY";

  /** Serialize the RPC results to a response object. */
  public VprGetPatientData.Response fromResult(List<RpcInvocationResult> results) {
    // TO-DO implement with help of XmlResponseRpc utility class
    return Response.builder()
        .results(
            results.stream()
                .filter(r -> r.error().isEmpty())
                .map(r -> r.response())
                .map(r -> XmlResponseRpc.deserialize(r, Response.Results.class))
                .collect(Collectors.toList()))
        .build();
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

    @Data
    static class Results {
      List<Vital> vitals;
    }
  }

  @Builder
  static class Request implements TypeSafeRpcRequest {
    String icn;

    Set<Domains> domains;

    Instant start;

    Instant stop;

    String max;

    String item;

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
                  RpcDetails.Parameter.builder()
                      .string(start != null ? start.toString() : null)
                      .build(),
                  RpcDetails.Parameter.builder()
                      .string(stop != null ? stop.toString() : null)
                      .build(),
                  RpcDetails.Parameter.builder().string(max).build(),
                  RpcDetails.Parameter.builder().string(item).build(),
                  RpcDetails.Parameter.builder().array(filter).build()))
          .build();
    }
  }
}
