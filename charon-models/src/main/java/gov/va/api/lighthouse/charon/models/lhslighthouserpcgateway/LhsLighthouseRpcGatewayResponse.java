package gov.va.api.lighthouse.charon.models.lhslighthouserpcgateway;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import gov.va.api.lighthouse.charon.models.TypeSafeRpcResponse;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Java model of the Lighthouse Gateway RPC's string response for (de)serialization. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class LhsLighthouseRpcGatewayResponse implements TypeSafeRpcResponse {
  private Map<String, Results> resultsByStation;

  /** Lazy Initialization. */
  public Map<String, Results> resultsByStation() {
    if (resultsByStation == null) {
      resultsByStation = Map.of();
    }
    return resultsByStation;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonAutoDetect(
      fieldVisibility = JsonAutoDetect.Visibility.ANY,
      isGetterVisibility = JsonAutoDetect.Visibility.NONE)
  public static class Results {

    // ToDo add metadata

    private List<FilemanEntry> results;

    /** Lazy Initialization. */
    public List<FilemanEntry> results() {
      if (results == null) {
        return List.of();
      }
      return results;
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonAutoDetect(
      fieldVisibility = JsonAutoDetect.Visibility.ANY,
      isGetterVisibility = JsonAutoDetect.Visibility.NONE)
  public static class FilemanEntry {
    private Map<String, Values> fields;

    private String file;

    private String ien;

    /** Lazy Initialization. */
    public Map<String, Values> resultsByStation() {
      if (fields == null) {
        fields = Map.of();
      }
      return fields;
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor(staticName = "of")
  @JsonAutoDetect(
      fieldVisibility = JsonAutoDetect.Visibility.ANY,
      isGetterVisibility = JsonAutoDetect.Visibility.NONE)
  public static class Values {
    String ext;

    String in;
  }
}
