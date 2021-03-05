package gov.va.api.lighthouse.charon.service.config;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ConnectionDetails {
  String name;
  String host;
  int port;
  String divisionIen;
  String timezone;
}
