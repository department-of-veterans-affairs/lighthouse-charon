package gov.va.api.lighthouse.vistalink.service.api;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class RpcPrincipal {
  @NonNull private String accessCode;
  @NonNull private String verifyCode;
}
