package gov.va.api.lighthouse.charon.service.config;

import static gov.va.api.lighthouse.charon.service.controller.AlternateAuthorizationStatusIds.AlternateAuthorizationStatusIdsDisabled;
import static gov.va.api.lighthouse.charon.service.controller.AlternateAuthorizationStatusIds.AuthorizationId;
import static gov.va.api.lighthouse.charon.service.controller.AlternateAuthorizationStatusIds.MappedAlternateAuthorizationStatusIds;

import gov.va.api.lighthouse.charon.service.controller.AlternateAuthorizationStatusIds;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AlternateAuthorizationStatusIdConfig {

  @Bean
  AlternateAuthorizationStatusIds alternateAuthorizationStatusIds(
      @Autowired AlternateAuthorizationStatusIdProperties properties) {
    if (properties.isEnabled()) {
      log.info("Alternate Authorization Status IDs are enabled.");
      return MappedAlternateAuthorizationStatusIds.builder()
          .publicToPrivateIds(load(properties.getId()))
          .build();
    }
    return new AlternateAuthorizationStatusIdsDisabled();
  }

  private Map<AuthorizationId, AuthorizationId> load(Map<String, String> rawIds) {
    Map<AuthorizationId, AuthorizationId> publicToPrivateAuthorizationIds = new HashMap<>();
    for (Map.Entry<String, String> entry : rawIds.entrySet()) {
      AuthorizationId publicId = AuthorizationId.of(entry.getKey());
      AuthorizationId privateId = AuthorizationId.of(entry.getValue());
      publicToPrivateAuthorizationIds.put(publicId, privateId);
    }
    return publicToPrivateAuthorizationIds;
  }
}
