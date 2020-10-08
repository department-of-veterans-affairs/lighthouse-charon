package gov.va.api.lighthouse.vistalink.service.api;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcDetails {
  private String name;
  private String context;
  private List<String> parameters;
}
