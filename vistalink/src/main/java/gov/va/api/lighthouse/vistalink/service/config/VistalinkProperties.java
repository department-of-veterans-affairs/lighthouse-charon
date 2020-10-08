package gov.va.api.lighthouse.vistalink.service.config;

import java.util.List;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class VistalinkProperties {

  List<ConnectionDetails> vistas;
}
