package gov.va.api.lighthouse.charon.service.config;

import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("clinical-authorization-status")
@Data
@Accessors(fluent = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClinicalAuthorizationStatusProperties {
  String accessCode;
  String verifyCode;
  String applicationProxyUser;

  /** Get an RpcPrincipal for using the clinical authorization vpc. */
  public RpcPrincipal principal() {
    return RpcPrincipal.builder()
        .accessCode(getAccessCode())
        .verifyCode(getVerifyCode())
        .applicationProxyUser(getApplicationProxyUser())
        .build();
  }
}
