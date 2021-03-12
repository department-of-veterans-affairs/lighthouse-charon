package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcResponse;

public interface MacroExecutionContext {
  ConnectionDetails connectionDetails();

  RpcResponse invoke(RpcRequest request);
}
