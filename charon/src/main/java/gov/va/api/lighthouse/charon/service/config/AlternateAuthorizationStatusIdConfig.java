package gov.va.api.lighthouse.charon.service.config;

import static gov.va.api.lighthouse.charon.service.controller.AlternateAuthorizationStatusIds.AlternateAuthorizationStatusIdsDisabled;
import static gov.va.api.lighthouse.charon.service.controller.AlternateAuthorizationStatusIds.AlternateAuthorizationStatusIdsEnabled;

import gov.va.api.lighthouse.charon.service.controller.AlternateAuthorizationStatusIds;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration for enabling alternate-id swaps. */
@Configuration
@Slf4j
public class AlternateAuthorizationStatusIdConfig {

  @Bean
  AlternateAuthorizationStatusIds alternateAuthorizationStatusIds(
      @Autowired AlternateAuthorizationStatusIdProperties properties) {
    if (properties.isEnabled()) {
      log.info("Alternate authorization status IDs are enabled");
      return AlternateAuthorizationStatusIdsEnabled.builder()
          .publicToPrivateIds(load(properties.getIds()))
          .build();
    }
    log.info("Alternate authorization status IDs are disabled");
    return new AlternateAuthorizationStatusIdsDisabled();
  }

  private IllegalArgumentException badFormat(String duzAtSiteColonDuzAtSite) {
    return new IllegalArgumentException(
        "Expected format duz@site:duz@site, got: " + duzAtSiteColonDuzAtSite);
  }

  private Map<AuthorizationId, AuthorizationId> load(List<String> duzAtSiteColonDuzAtSites) {
    var map = new HashMap<AuthorizationId, AuthorizationId>(duzAtSiteColonDuzAtSites.size());
    for (String duzAtSiteColonDuzAtSite : duzAtSiteColonDuzAtSites) {
      int colon = duzAtSiteColonDuzAtSite.indexOf(':');
      /* The : is missing, or near the beginning or end of string. */
      if (colon < 2 || colon >= duzAtSiteColonDuzAtSite.length() - 2) {
        throw badFormat(duzAtSiteColonDuzAtSite);
      }
      String from = duzAtSiteColonDuzAtSite.substring(0, colon);
      String to = duzAtSiteColonDuzAtSite.substring(colon + 1);
      map.put(AuthorizationId.of(from), AuthorizationId.of(to));
    }
    log.info("{} alternate IDs loaded", map.size());
    return map;
  }
}
