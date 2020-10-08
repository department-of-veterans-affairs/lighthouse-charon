package gov.va.api.lighthouse.vistalink.service.api;

import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcResponse {
  private Optional<String> message;
  private List<RpcInvocationResult> results;
}
