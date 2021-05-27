package gov.va.api.lighthouse.charon.models;

import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import java.util.List;

/** Interface for creating TypeSafeRpc interactions. */
public interface TypeSafeRpc<R extends TypeSafeRpcRequest, P extends TypeSafeRpcResponse> {
  TypeSafeRpcResponse fromResults(List<RpcInvocationResult> results);
}
