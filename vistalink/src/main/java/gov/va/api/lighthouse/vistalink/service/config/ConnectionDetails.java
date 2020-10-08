package gov.va.api.lighthouse.vistalink.service.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionDetails {
  private String host;
  private int port;
  private String divisionIen;
}
