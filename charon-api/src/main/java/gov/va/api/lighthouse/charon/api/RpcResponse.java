package gov.va.api.lighthouse.charon.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

/** Contains all response data related to a request. */
@Data
@Builder
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RpcResponse {
  private Status status;
  private Optional<String> message;
  private List<RpcInvocationResult> results;

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

  /** All known response states for a request. */
  public enum Status {
    OK,
    FAILED,
    NO_VISTAS_RESOLVED,
    VISTA_RESOLUTION_FAILURE
  }
}
