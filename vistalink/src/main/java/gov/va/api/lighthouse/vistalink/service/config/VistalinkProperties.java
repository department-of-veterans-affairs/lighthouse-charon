package gov.va.api.lighthouse.vistalink.service.config;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Builder
@Value
public class VistalinkProperties {
  @Singular List<ConnectionDetails> vistas;
}
