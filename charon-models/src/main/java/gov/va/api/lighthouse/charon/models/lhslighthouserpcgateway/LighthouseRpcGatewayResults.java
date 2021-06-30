package gov.va.api.lighthouse.charon.models.lhslighthouserpcgateway;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LighthouseRpcGatewayResults {

  private List<FilemanEntry> results;

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
