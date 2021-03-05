package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;

/** This warning is a bug in the OpenJDK, see https://bugs.openjdk.java.net/browse/JDK-8155591. */
@SuppressWarnings("try")
public interface RpcInvoker extends AutoCloseable {

  // redefined to remove exception from the signature
  @Override
  void close();

  RpcInvocationResult invoke(RpcDetails rpcDetails);

  String vista();
}
