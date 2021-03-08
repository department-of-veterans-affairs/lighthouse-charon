package gov.va.api.lighthouse.charon.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcMetadata {
  private String timezone;
}
