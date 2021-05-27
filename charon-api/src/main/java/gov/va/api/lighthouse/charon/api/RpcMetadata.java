package gov.va.api.lighthouse.charon.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import lombok.Builder;
import lombok.Data;

/** Metadata related to the rpc request. */
@Data
@Builder
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RpcMetadata {
  private String timezone;
}
