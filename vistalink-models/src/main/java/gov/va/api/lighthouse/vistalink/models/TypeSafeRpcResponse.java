package gov.va.api.lighthouse.vistalink.models;

import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;
import java.util.List;

public interface TypeSafeRpcResponse {
  TypeSafeRpcResponse fromResult(List<RpcInvocationResult> results);
}
