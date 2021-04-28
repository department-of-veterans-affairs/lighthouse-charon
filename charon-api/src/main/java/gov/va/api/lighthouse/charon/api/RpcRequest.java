package gov.va.api.lighthouse.charon.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RpcRequest {
  @NotNull @Valid private RpcDetails rpc;
  @NotNull @Valid private RpcPrincipal principal;
  private Map<String, @Valid RpcPrincipal> siteSpecificPrincipals;
  @NotNull @Valid private RpcVistaTargets target;

  @AssertTrue(message = "principal cannot specify contextOverride")
  private boolean isPrincipalContextOverrideUnset() {
    return principal.contextOverride() == null;
  }

  /** Lazy initializer. */
  public Map<String, RpcPrincipal> siteSpecificPrincipals() {
    if (siteSpecificPrincipals == null) {
      siteSpecificPrincipals = new HashMap<>();
    }
    return siteSpecificPrincipals;
  }
}
