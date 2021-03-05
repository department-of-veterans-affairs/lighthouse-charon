package gov.va.api.lighthouse.charon.models.vprgetpatientdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import gov.va.api.lighthouse.charon.models.TypeSafeRpc;
import gov.va.api.lighthouse.charon.models.TypeSafeRpcRequest;
import gov.va.api.lighthouse.charon.models.TypeSafeRpcResponse;
import gov.va.api.lighthouse.charon.models.XmlResponseRpc;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@NoArgsConstructor(staticName = "create")
public class VprGetPatientData
    implements TypeSafeRpc<VprGetPatientData.Request, VprGetPatientData.Response> {
  private static final String RPC_NAME = "VPR GET PATIENT DATA";

  private static final String RPC_CONTEXT = "VPR APPLICATION PROXY";

  /** Serialize the RPC results to a response object. */
  @Override
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
   * Start and stop are currently supported as fileman date strings. Later a macro will be used to
   * change an ISO 8601 into a fileman date for the target VistA's correct timezone.
   */
  @Builder
  public static class Request implements TypeSafeRpcRequest {
    @NonNull private PatientId dfn;

    private Set<Domains> type;

    private Optional<String> start;

    private Optional<String> stop;

    private Optional<String> max;

    private Optional<String> id;

    private List<String> filter;

    /** Build RpcDetails out of the request. */
    @Override
    public RpcDetails asDetails() {
      return RpcDetails.builder()
          .context(RPC_CONTEXT)
          .name(RPC_NAME)
          .parameters(
              List.of(
                  RpcDetails.Parameter.builder().string(dfn.toString()).build(),
                  RpcDetails.Parameter.builder()
                      .string(
                          type().stream()
                              .filter(Objects::nonNull)
                              .map(Enum::name)
                              .collect(Collectors.joining(";")))
                      .build(),
                  RpcDetails.Parameter.builder().string(start().orElse("")).build(),
                  RpcDetails.Parameter.builder().string(stop().orElse("")).build(),
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
    Optional<String> start() {
      if (start == null) {
        start = Optional.empty();
      }
      return start;
    }

    /** Lazy getter. */
    Optional<String> stop() {
      if (stop == null) {
        stop = Optional.empty();
      }
      return stop;
    }

    /** Lazy getter. */
    Set<Domains> type() {
      if (type == null) {
        type = new HashSet<>();
      }
      return type;
    }

    @Value
    public static class PatientId {
      String dfn;
      String icn;

      /** You must specify dfn or icn, or both. */
      @Builder
      public PatientId(String dfn, String icn) {
        this.dfn = dfn;
        this.icn = icn;
        if (dfn == null && icn == null) {
          throw new IllegalArgumentException("At least one of DFN or ICN must be specified.");
        }
      }

      public static PatientId forDfn(String dfn) {
        return new PatientId(dfn, null);
      }

      public static PatientId forIcn(String icn) {
        return new PatientId(null, icn);
      }

      @Override
      public String toString() {
        StringBuilder s = new StringBuilder();
        if (dfn != null) {
          s.append(dfn);
        }
        if (icn != null) {
          s.append(';').append(icn);
        }
        return s.toString();
      }
    }
  }

  @Data
  @Builder
  public static class Response implements TypeSafeRpcResponse {
    private Map<String, Results> resultsByStation;

    @AllArgsConstructor
    @Builder
    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @JacksonXmlRootElement(localName = "results")
    public static class Results {
      @JacksonXmlProperty(isAttribute = true)
      private String version;

      @JacksonXmlProperty(isAttribute = true)
      private String timeZone;

      @JacksonXmlProperty private Labs labs;

      @JacksonXmlProperty private Vitals vitals;

      /** Get a stream of labs for a patient. */
      @JsonIgnore
      public Stream<Labs.Lab> labStream() {
        if (labs() == null) {
          return Stream.empty();
        }
        return labs().labResults().stream();
      }

      /** Get a stream of vitals for a patient. */
      @JsonIgnore
      public Stream<Vitals.Vital> vitalStream() {
        if (vitals() == null) {
          return Stream.empty();
        }
        return vitals().vitalResults().stream();
      }
    }
  }
}
