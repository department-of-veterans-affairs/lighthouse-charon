package gov.va.api.lighthouse.charon.models.lhslighthouserpcgateway;

import gov.va.api.lighthouse.charon.models.TypeSafeRpcResponse;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

/** Java model of the Lighthouse Gateway RPC's string response for (de)serialization. */
@Data
@Builder
public class LhsLighthouseRpcGatewayResponse implements TypeSafeRpcResponse {
  private Map<String, Results> resultsByStation;

  /** Lazy Initialization. */
  public Map<String, Results> resultsByStation() {
    if (resultsByStation == null) {
      resultsByStation = Map.of();
    }
    return resultsByStation;
  }

  @Value
  @Builder
  public static class Results {

    // ToDo add metadata

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
