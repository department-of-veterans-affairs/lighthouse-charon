package gov.va.api.lighthouse.charon.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class RpcResponse {
  private Status status;
  private Optional<String> message;
  @Singular private List<RpcInvocationResult> results;

  /** Lazy getter. */
  public Optional<String> message() {
    if (message == null) {
      message = Optional.empty();
    }
    return message;
  }

  /** Lazy getter. */
  public List<RpcInvocationResult> results() {
    if (results == null) {
      results = new ArrayList<>();
    }
    return results;
  }

  public enum Status {
    OK,
    FAILED,
    NO_VISTAS_RESOLVED,
    VISTA_RESOLUTION_FAILURE
  }
}
