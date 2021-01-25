package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import gov.va.api.lighthouse.vistalink.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.models.TypeSafeRpc;
import gov.va.api.lighthouse.vistalink.models.TypeSafeRpcResponse;
import gov.va.api.lighthouse.vistalink.models.XmlResponseRpc;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.Data;

public class VprGetPatientData
    implements TypeSafeRpc<VprGetPatientData.Request, VprGetPatientData.Response> {
  private static final String RPC_NAME = "VPR GET PATIENT DATA";

  private static final String RPC_CONTEXT = "VPR APPLICATION PROXY";

  /** Serialize the RPC results to a response object. */
  public VprGetPatientData.Response fromResults(List<RpcInvocationResult> invocationResults) {
    // TO-DO implement with help of XmlResponseRpc utility class
    return Response.builder()
        .results(
            invocationResults.stream()
                .filter(invocationResult -> invocationResult.error().isEmpty())
                .map(invocationResult -> invocationResult.response())
                .map(response -> XmlResponseRpc.deserialize(response, Response.Results.class))
                .collect(Collectors.toList()))
        .build();
  }

  enum Domains {
    appointments,
    consults,
    demographics,
    documents,
    education,
    exams,
    factors,
    flags,
    immunizations,
    insurance,
    labs,
    meds,
    observations,
    problems,
    procedures,
    reactions,
    reminders,
    skinTests,
    visits,
    vitals
  }

  @Data
  @Builder
  static class Response implements TypeSafeRpcResponse {
    List<Results> results;

    @Data
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    static class Results {
      @XmlAttribute String version;
      @XmlAttribute String timeZone;
      List<Vital> vitals;
    }
  }

  @Builder
  static class Request implements TypeSafeRpcRequest {
    String dfn;

    Set<Domains> type;

    String max;

    String id;

    List<String> filter;

    public RpcDetails asDetails() {
      return RpcDetails.builder()
          .context(RPC_CONTEXT)
          .name(RPC_NAME)
          .parameters(
              List.of(
                  RpcDetails.Parameter.builder().string(dfn).build(),
                  RpcDetails.Parameter.builder()
                      .array(type.stream().map(Enum::name).collect(Collectors.toList()))
                      .build(),
                  RpcDetails.Parameter.builder().string(max).build(),
                  RpcDetails.Parameter.builder().string(id).build(),
                  RpcDetails.Parameter.builder().array(filter).build()))
          .build();
    }
  }
}
