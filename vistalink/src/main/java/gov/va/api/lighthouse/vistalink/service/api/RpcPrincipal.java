package gov.va.api.lighthouse.vistalink.service.api;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class RpcPrincipal {
  @NotBlank @NonNull private String accessCode;
  @NotBlank @NonNull private String verifyCode;
}
