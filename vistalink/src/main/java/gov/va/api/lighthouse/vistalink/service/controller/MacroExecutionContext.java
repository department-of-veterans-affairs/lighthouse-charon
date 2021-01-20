package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.api.RpcResponse;

public interface MacroExecutionContext {
  RpcResponse invoke(RpcRequest request);
}
