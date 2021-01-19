package gov.va.api.lighthouse.vistalink.models;

import gov.va.api.lighthouse.vistalink.api.RpcDetails;
import java.util.List;

public interface VistaRpc {

  /** Build RPC details from provided parameters and RPC specific name and context. */
  default RpcDetails buildRpcDetails(List<RpcDetails.Parameter> parameters) {
    return RpcDetails.builder()
        .parameters(parameters)
        .name(rpcName())
        .context(rpcContext())
        .build();
  }

  <T> T deserialize(String response, Class<T> clazz);

  default String rpcContext() {
    return null;
  }

  default String rpcName() {
    return null;
  }
}
