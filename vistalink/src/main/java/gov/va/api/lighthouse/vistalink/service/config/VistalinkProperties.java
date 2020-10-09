package gov.va.api.lighthouse.vistalink.service.config;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VistalinkProperties {
  private List<ConnectionDetails> vistas;
}
