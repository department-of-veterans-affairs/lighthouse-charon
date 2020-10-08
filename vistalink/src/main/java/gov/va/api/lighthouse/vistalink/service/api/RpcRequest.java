package gov.va.api.lighthouse.vistalink.service.api;

import lombok.Data;

@Data
public class RpcRequest {

  RpcDetails rpc;
  RpcPrincipal principal;
  RpcVistaTargets target;
}
