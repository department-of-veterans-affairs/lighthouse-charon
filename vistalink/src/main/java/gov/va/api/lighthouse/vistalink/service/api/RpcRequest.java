package gov.va.api.lighthouse.vistalink.service.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcRequest {
  RpcDetails rpc;
  RpcPrincipal principal;
  RpcVistaTargets target;

  void test() {
    RpcRequest.builder().build();
  }
}

