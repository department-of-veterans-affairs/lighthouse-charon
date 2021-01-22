package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcResponse;

public interface MacroExecutionContext {
  RpcResponse invoke(RpcRequest request);
}
