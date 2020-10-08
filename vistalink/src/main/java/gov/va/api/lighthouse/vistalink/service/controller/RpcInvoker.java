package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.service.api.RpcInvocationResult;

public interface RpcInvoker extends AutoCloseable {

  void close();

  RpcInvocationResult invoke(RpcDetails rpcDetails);
}