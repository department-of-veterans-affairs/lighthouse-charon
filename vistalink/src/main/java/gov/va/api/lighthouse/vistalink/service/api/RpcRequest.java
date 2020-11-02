package gov.va.api.lighthouse.vistalink.service.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcRequest {
  @NotNull @Valid private RpcDetails rpc;
  @NotNull @Valid private RpcPrincipal principal;
  @NotNull @Valid private RpcVistaTargets target;
}
