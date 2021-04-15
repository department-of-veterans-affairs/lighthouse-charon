package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.service.config.AlternateAuthorizationStatusIdConfig;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public interface AlternateAuthorizationStatusIds {

    /**
     * Returns the private ID for the given public authorization ID. If no such private ID is available, return the given public ID.
     */
    AuthorizationId toPrivateId(AuthorizationId authorizationId);

    @Value
    @Builder
    class AuthorizationId {
        String duz;
        String site;

        public static AuthorizationId of(String rawId) {
            String[] rawIdPieces = rawId.split("@", -1);
            if(rawIdPieces.length != 2) {
                throw new IllegalArgumentException("Alternate Authorization ID cannot be parsed. Authorization IDs Should be of format duz123@123");
            }
            return AuthorizationId.builder().duz(rawIdPieces[0]).site(rawIdPieces[1]).build();
        }
    }

    @Builder
    @Value
    class MappedAlternateAuthorizationStatusIds implements AlternateAuthorizationStatusIds {

        Map<AuthorizationId, AuthorizationId> publicToPrivateIds;

        @Override
        public AuthorizationId toPrivateId(AuthorizationId publicId) {
            return publicToPrivateIds.getOrDefault(publicId, publicId);
        }
    }

    class AlternateAuthorizationStatusIdsDisabled implements AlternateAuthorizationStatusIds {

        @Override
        public AuthorizationId toPrivateId(AuthorizationId authorizationId) {
            return authorizationId;
        }
    }
}
