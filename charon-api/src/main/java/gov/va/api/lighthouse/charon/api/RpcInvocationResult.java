package gov.va.api.lighthouse.charon.api;

import java.util.Optional;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcInvocationResult {
  private String vista;
  private RpcMetadata metadata;
  private String response;
  private Optional<String> error;

  /** Lazy getter. */
  public Optional<String> error() {
    if (error == null) {
      error = Optional.empty();
    }
    return error;
  }
}
