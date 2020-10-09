package gov.va.api.lighthouse.vistalink.service.api;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcVistaTargets {
  private String forPatient;
  private List<String> include;
  private List<String> exclude;
}
