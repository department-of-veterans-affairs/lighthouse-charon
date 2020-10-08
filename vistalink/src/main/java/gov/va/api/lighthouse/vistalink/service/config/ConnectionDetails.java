package gov.va.api.lighthouse.vistalink.service.config;

import lombok.Data;

@Data
public class ConnectionDetails {
  String host;
  int port;
  String divisionIen;
}
