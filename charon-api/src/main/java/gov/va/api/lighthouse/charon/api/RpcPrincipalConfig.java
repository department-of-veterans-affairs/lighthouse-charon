package gov.va.api.lighthouse.charon.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Contains all known principal information. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RpcPrincipalConfig {

  @NotEmpty @Valid private List<PrincipalEntry> rpcPrincipals;

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class PrincipalEntry {
    @NotEmpty private List<String> rpcNames;

    private String applicationProxyUser;

    @NotEmpty @Valid private List<Codes> codes;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Codes {
    @NotEmpty private List<String> sites;
    @NotBlank private String accessCode;
    @NotBlank private String verifyCode;
  }
}
