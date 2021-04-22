package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/** Provides selection of the best principal for the target. */
@AllArgsConstructor(staticName = "of")
public class PrincipalResolution {
  @NonNull private final RpcRequest request;

  /** Return the best credentials for the target. */
  public RpcPrincipal resolve(@NonNull ConnectionDetails target) {
    return request.siteSpecificPrincipals().getOrDefault(target.name(), request.principal());
  }
}
