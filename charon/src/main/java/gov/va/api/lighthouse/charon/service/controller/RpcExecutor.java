package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;

public interface RpcExecutor {
  RpcResponse execute(RpcRequest request);
}
