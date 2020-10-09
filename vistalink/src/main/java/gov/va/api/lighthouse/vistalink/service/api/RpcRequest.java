package gov.va.api.lighthouse.vistalink.service.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcRequest {
  private RpcDetails rpc;
  private RpcPrincipal principal;
  private RpcVistaTargets target;
}
