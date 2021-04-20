package gov.va.api.lighthouse.charon.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RpcRequest {
  @NotNull @Valid private RpcDetails rpc;
  @NotNull @Valid private RpcPrincipal principal;
  @NotNull @Valid private RpcVistaTargets target;
}
