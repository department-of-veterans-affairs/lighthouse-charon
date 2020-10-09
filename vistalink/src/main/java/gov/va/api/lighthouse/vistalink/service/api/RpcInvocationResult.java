package gov.va.api.lighthouse.vistalink.service.api;

import java.util.Optional;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcInvocationResult {
  private String vista;
  private String response;
  private Optional<String> error;
}
