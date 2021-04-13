package gov.va.api.lighthouse.charon.service.config;

import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("clinical-authorization-status")
@Data
@Accessors(fluent = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class ClinicalAuthorizationStatusProperties {
  @NotBlank @NonNull private String accessCode;
  @NotBlank @NonNull private String verifyCode;
  @NotBlank @NonNull private String applicationProxyUser;
  @NotBlank @NonNull private String defaultMenuOption;

  /** Get an RpcPrincipal for using the clinical authorization vpc. */
  public RpcPrincipal principal() {
    return RpcPrincipal.builder()
        .accessCode(getAccessCode())
        .verifyCode(getVerifyCode())
        .applicationProxyUser(getApplicationProxyUser())
        .build();
  }
}
