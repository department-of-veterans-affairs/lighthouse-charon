package gov.va.api.lighthouse.vistalink.service.config;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ConnectionDetails {
  String name;
  String host;
  String port;
  String divisionIen;
}
