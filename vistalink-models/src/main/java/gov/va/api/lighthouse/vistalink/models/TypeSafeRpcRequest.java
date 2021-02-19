package gov.va.api.lighthouse.vistalink.models;

import gov.va.api.lighthouse.vistalink.api.RpcDetails;

public interface TypeSafeRpcRequest {
  RpcDetails asDetails();
}
