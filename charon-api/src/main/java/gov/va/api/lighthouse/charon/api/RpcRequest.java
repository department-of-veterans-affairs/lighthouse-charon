package gov.va.api.lighthouse.charon.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/** Contains all information related to an RpcRequest. */
@Data
@Builder
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RpcRequest {
  @NotNull @Valid private RpcDetails rpc;
  @NotNull @Valid private RpcPrincipal principal;
  private Map<String, @Valid RpcPrincipal> siteSpecificPrincipals;
  @NotNull @Valid private RpcVistaTargets target;

  /** Lazy initializer. */
  public Map<String, RpcPrincipal> siteSpecificPrincipals() {
    if (siteSpecificPrincipals == null) {
      siteSpecificPrincipals = new HashMap<>();
    }
    return siteSpecificPrincipals;
  }
}
