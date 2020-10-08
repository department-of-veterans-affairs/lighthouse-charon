package gov.va.api.lighthouse.vistalink.service.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionDetails {
  String host;
  int port;
  String divisionIen;
}
