package gov.va.api.lighthouse.charon.service.config;

import gov.va.api.lighthouse.charon.service.controller.AlternateAuthorizationStatusIds;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static gov.va.api.lighthouse.charon.service.controller.AlternateAuthorizationStatusIds.*;

@Configuration
@Slf4j
public class AlternateAuthorizationStatusIdConfig {

    @Bean
    AlternateAuthorizationStatusIds alternateAuthorizationStatusIds(
            @Autowired AlternateAuthorizationStatusIdProperties properties
    ) {
        if(properties.isEnabled()) {
            log.info("Alternate Authorization Status IDs are enabled.");
            return MappedAlternateAuthorizationStatusIds.builder()
                    .publicToPrivateIds(load(properties.getId()))
                    .build();
        }
        return new AlternateAuthorizationStatusIdsDisabled();
    }

    private Map<AuthorizationId, AuthorizationId> load(Map<String, String> rawIds) {
        Map<AuthorizationId, AuthorizationId> publicToPrivateAuthorizationIds = new HashMap<>();
        for(String k: rawIds.keySet()) {
            AuthorizationId publicId = AuthorizationId.of(k);
            AuthorizationId privateId = AuthorizationId.of(rawIds.get(k));
            publicToPrivateAuthorizationIds.put(publicId, privateId);
        }
        return publicToPrivateAuthorizationIds;
    }
}
