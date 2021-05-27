package gov.va.api.lighthouse.charon.api;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RpcPrincipal {
  /** Required for standard user, application proxy user. */
  @NotBlank @NonNull private String accessCode;
  /** Required for standard user, application proxy user. */
  @NotBlank @NonNull private String verifyCode;
  /** Required for application proxy user. */
  private String applicationProxyUser;
  /**
   * For site specific principals, a different RPC context can be specified. This field is not valid
   * for the default RPC request principal.
   */
  private String contextOverride;

  @Builder(builderMethodName = "standardUserBuilder", builderClassName = "StandardUserBuilder")
  private RpcPrincipal(
      @NonNull String accessCode, @NonNull String verifyCode, String contextOverride) {
    this.accessCode = accessCode;
    this.verifyCode = verifyCode;
    this.contextOverride = contextOverride;
  }

  @JsonCreator
  @Builder(
      builderMethodName = "applicationProxyUserBuilder",
      builderClassName = "ApplicationProxyUserBuilder")
  private RpcPrincipal(
      @JsonProperty("accessCode") @NonNull String accessCode,
      @JsonProperty("verifyCode") @NonNull String verifyCode,
      @JsonProperty("applicationProxyUser") String applicationProxyUser,
      @JsonProperty("contextOverride") String contextOverride) {
    this.accessCode = accessCode;
    this.verifyCode = verifyCode;
    this.applicationProxyUser = applicationProxyUser;
    this.contextOverride = contextOverride;
  }

  @SuppressWarnings("unused")
  @JsonIgnore
  @AssertTrue(message = "Invalid property combination.")
  private boolean isValid() {
    return type() != LoginType.INVALID;
  }

  /**
   * Determine the type of principal information. INVALID will be returned if enough information is
   * not available to satisfy any login type.
   */
  @JsonIgnore
  public LoginType type() {
    if (isNotBlank(accessCode())
        && isNotBlank(verifyCode())
        && isNotBlank(applicationProxyUser())) {
      return LoginType.APPLICATION_PROXY_USER;
    }
    if (isNotBlank(accessCode()) && isNotBlank(verifyCode())) {
      return LoginType.STANDARD_USER;
    }
    return LoginType.INVALID;
  }

  public enum LoginType {
    STANDARD_USER,
    APPLICATION_PROXY_USER,
    INVALID
  }
}
