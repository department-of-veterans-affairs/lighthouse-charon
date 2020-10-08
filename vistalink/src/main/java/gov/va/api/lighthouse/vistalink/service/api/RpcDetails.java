package gov.va.api.lighthouse.vistalink.service.api;

import java.util.List;
import lombok.Data;

@Data
public class RpcDetails {
  String name;
  String context;
  List<String> parameters;
}
