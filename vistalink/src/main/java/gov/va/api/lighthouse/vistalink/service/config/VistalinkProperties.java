package gov.va.api.lighthouse.vistalink.service.config;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Builder
@Data
@Component
public class VistalinkProperties {
  private List<ConnectionDetails> vistas;
}
