package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;

/** Interface for defining how requests are executed against rpcs. */
public interface RpcExecutor {
  RpcResponse execute(RpcRequest request);
}
