package gov.va.api.lighthouse.charon.models;

import gov.va.api.lighthouse.charon.api.RpcDetails;

/** Interface for creating type safe rpc requests. */
public interface TypeSafeRpcRequest {
  RpcDetails asDetails();
}
