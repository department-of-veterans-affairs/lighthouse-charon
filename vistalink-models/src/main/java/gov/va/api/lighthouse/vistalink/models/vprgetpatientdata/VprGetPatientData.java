package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import gov.va.api.lighthouse.vistalink.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.models.TypeSafeRpc;
import gov.va.api.lighthouse.vistalink.models.TypeSafeRpcRequest;
import gov.va.api.lighthouse.vistalink.models.TypeSafeRpcResponse;
import gov.va.api.lighthouse.vistalink.models.XmlResponseRpc;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        .resultsByStation(
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

    Set<Domains> type;

    Optional<String> max;

    Optional<String> id;

    List<String> filter;

    /** Build RpcDetails out of the request. */
    public RpcDetails asDetails() {
      return RpcDetails.builder()
          .context(RPC_CONTEXT)
          .name(RPC_NAME)
          .parameters(
              List.of(
                  RpcDetails.Parameter.builder().string(dfn).build(),
                  RpcDetails.Parameter.builder()
                      .string(
                          type().stream()
                              .filter(Objects::nonNull)
                              .map(Enum::name)
                              .collect(Collectors.joining(";")))
                      .build(),
                  RpcDetails.Parameter.builder().string("").build(),
                  RpcDetails.Parameter.builder().string("").build(),
                  RpcDetails.Parameter.builder().string(max().orElse("")).build(),
                  RpcDetails.Parameter.builder().string(id().orElse("")).build(),
                  RpcDetails.Parameter.builder().array(filter()).build()))
          .build();
    }

    /** Lazy getter. */
    List<String> filter() {
      if (filter == null) {
        filter = new ArrayList<>();
      }
      return filter;
    }

    /** Lazy getter. */
    Optional<String> id() {
      if (id == null) {
        id = Optional.empty();
      }
      return id;
    }

    /** Lazy getter. */
    Optional<String> max() {
      if (max == null) {
        max = Optional.empty();
      }
      return max;
    }

    /** Lazy getter. */
    Set<Domains> type() {
      if (type == null) {
        type = new HashSet<>();
      }
      return type;
    }
  }

  @Data
  @Builder
  public static class Response implements TypeSafeRpcResponse {
    Map<String, Results> resultsByStation;

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

      @JacksonXmlProperty(localName = "vitals")
      Vitals vitalInformation;

      /** Determine if array of values are all null. */
      private static boolean allNull(Object... values) {
        for (Object value : values) {
          if (value != null) {
            return false;
          }
        }
        return true;
      }

      /** Get the list of vitals for a patient. */
      public List<Vitals.Vital> vitals() {
        if (vitalInformation() == null) {
          return List.of();
        }
        // If even one field isn't null in a result, we should return it
        return vitalInformation().vitalResults().stream()
            .filter(
                result ->
                    !allNull(
                        result.entered(),
                        result.facility(),
                        result.location(),
                        result.measurements(),
                        result.removed(),
                        result.taken()))
            .collect(Collectors.toList());
      }
    }
  }
}
