package gov.va.api.lighthouse.vistalink.service.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcPrincipal {
  private String accessCode;
  private String verifyCode;
}
