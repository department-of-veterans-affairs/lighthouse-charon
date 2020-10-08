package gov.va.api.lighthouse.vistalink.service.api;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcDetails {
  String name;
  String context;
  List<String> parameters;
}
