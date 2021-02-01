package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import gov.va.api.lighthouse.vistalink.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.models.TypeSafeRpc;
import gov.va.api.lighthouse.vistalink.models.TypeSafeRpcRequest;
import gov.va.api.lighthouse.vistalink.models.TypeSafeRpcResponse;
import gov.va.api.lighthouse.vistalink.models.XmlResponseRpc;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(staticName = "create")
public class VprGetPatientData
    implements TypeSafeRpc<VprGetPatientData.Request, VprGetPatientData.Response> {
  private static final String RPC_NAME = "VPR GET PATIENT DATA";

  private static final String RPC_CONTEXT = "VPR APPLICATION PROXY";

  /** Serialize the RPC results to a response object. */
  public VprGetPatientData.Response fromResults(List<RpcInvocationResult> invocationResults) {
    // TO-DO needs to handle different bad result behaviors (e.g. : ignore errors, ignore a
    // percentage, or throw exception on errors.)
    return Response.builder()
        .results(
            invocationResults.stream()
                .filter(invocationResult -> invocationResult.error().isEmpty())
                .collect(
                    Collectors.toMap(
                        invocationResult -> invocationResult.vista(),
                        invocationResult ->
                            XmlResponseRpc.deserialize(
                                invocationResult.response(), Response.Results.class))))
        .build();
  }

  public enum Domains {
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

  /**
   * Start and stop are currently not supported but empty parameters are added to the parameters
   * after type in their place because VistA cares about parameter order.
   */
  @Builder
  public static class Request implements TypeSafeRpcRequest {
    @NonNull String dfn;

    Optional<Set<Domains>> type;

    Optional<String> max;

    Optional<String> id;

    Optional<List<String>> filter;

    /** Build RpcDetails out of the request. */
    public RpcDetails asDetails() {
      return RpcDetails.builder()
          .context(RPC_CONTEXT)
          .name(RPC_NAME)
          .parameters(
              List.of(
                  RpcDetails.Parameter.builder().string(dfn).build(),
                  RpcDetails.Parameter.builder()
                      .array(
                          type.orElse(Collections.emptySet()).stream()
                              .map(Enum::name)
                              .collect(Collectors.toList()))
                      .build(),
                  RpcDetails.Parameter.builder().string("").build(),
                  RpcDetails.Parameter.builder().string("").build(),
                  RpcDetails.Parameter.builder().string(max.orElse("")).build(),
                  RpcDetails.Parameter.builder().string(id.orElse("")).build(),
                  RpcDetails.Parameter.builder()
                      .array(filter.orElse(Collections.emptyList()))
                      .build()))
          .build();
    }
  }

  @Data
  @Builder
  public static class Response implements TypeSafeRpcResponse {
    Map<String, Results> results;

    @AllArgsConstructor
    @Builder
    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @JacksonXmlRootElement(localName = "results")
    public static class Results {
      @JacksonXmlProperty(isAttribute = true)
      String version;

      @JacksonXmlProperty(isAttribute = true)
      String timeZone;

      @JacksonXmlProperty Vitals vitals;
    }
  }
}
