package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.service.config.AuthorizationId;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

public interface AlternateAuthorizationStatusIds {

  /**
   * Returns the private ID for the given public authorization ID. If no such private ID is
   * available, return the given public ID.
   */
  AuthorizationId toPrivateId(AuthorizationId authorizationId);

  class AlternateAuthorizationStatusIdsDisabled implements AlternateAuthorizationStatusIds {

    @Override
    public AuthorizationId toPrivateId(AuthorizationId authorizationId) {
      return authorizationId;
    }
  }

  @Builder
  @Value
  class AlternateAuthorizationStatusIdsEnabled implements AlternateAuthorizationStatusIds {

    Map<AuthorizationId, AuthorizationId> publicToPrivateIds;

    @Override
    public AuthorizationId toPrivateId(AuthorizationId publicId) {
      return publicToPrivateIds.getOrDefault(publicId, publicId);
    }
  }
}
