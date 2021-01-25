package gov.va.api.lighthouse.vistalink.models;

import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.models.vprgetpatientdata.TypeSafeRpcRequest;
import java.util.List;

public interface TypeSafeRpc<R extends TypeSafeRpcRequest, P extends TypeSafeRpcResponse> {
  TypeSafeRpcResponse fromResults(List<RpcInvocationResult> results);
}
