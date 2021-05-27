package gov.va.api.lighthouse.charon.service.config;

import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import javax.validation.constraints.AssertTrue;
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

/** Load configuration for Clinical Authorization from file to a config object. */
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

  @AssertTrue(message = "clinical-authorization-status.access-code is unset")
  @SuppressWarnings("unused")
  private boolean isAccessCodeSet() {
    return !accessCode.equals("unset");
  }

  @AssertTrue(message = "clinical-authorization-status.application-proxy-user is unset")
  @SuppressWarnings("unused")
  private boolean isApplicationProxyUserSet() {
    return !applicationProxyUser.equals("unset");
  }

  @AssertTrue(message = "clinical-authorization-status.default-menu-option is unset")
  @SuppressWarnings("unused")
  private boolean isDefailtMenuOptionSet() {
    return !defaultMenuOption.equals("unset");
  }

  @AssertTrue(message = "clinical-authorization-status.verify-code is unset")
  @SuppressWarnings("unused")
  private boolean isVerifyCodeSet() {
    return !verifyCode.equals("unset");
  }

  /** Get an RpcPrincipal for using the clinical authorization vpc. */
  public RpcPrincipal principal() {
    return RpcPrincipal.builder()
        .accessCode(getAccessCode())
        .verifyCode(getVerifyCode())
        .applicationProxyUser(getApplicationProxyUser())
        .build();
  }
}
