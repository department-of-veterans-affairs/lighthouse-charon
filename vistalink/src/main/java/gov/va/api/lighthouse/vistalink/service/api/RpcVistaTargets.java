package gov.va.api.lighthouse.vistalink.service.api;

import lombok.Data;

@Data
public class RpcVistaTargets {
  String forPatient;
  String include;
  String exclude;
}
