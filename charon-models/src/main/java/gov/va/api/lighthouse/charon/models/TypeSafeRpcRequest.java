package gov.va.api.lighthouse.charon.models;

import gov.va.api.lighthouse.charon.api.RpcDetails;

public interface TypeSafeRpcRequest {
  RpcDetails asDetails();
}
