package gov.va.api.lighthouse.charon.models;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import java.util.Optional;

public interface TypeSafeRpcRequest {
  RpcDetails asDetails();

  void updateContext(Optional<String> context);
}
