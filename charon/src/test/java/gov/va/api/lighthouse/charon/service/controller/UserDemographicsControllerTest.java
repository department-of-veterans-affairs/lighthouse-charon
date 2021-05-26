package gov.va.api.lighthouse.charon.service.controller;

import static gov.va.api.lighthouse.charon.service.controller.InteractiveTestSupport.localTampaConnectionDetails;
import static gov.va.api.lighthouse.charon.service.controller.InteractiveTestSupport.requirePropertyValue;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.config.VistalinkProperties;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@Slf4j
class UserDemographicsControllerTest {

  @Test
  @EnabledIfSystemProperty(named = "interactive", matches = "true")
  void properties() {
    ConnectionDetails tampa = localTampaConnectionDetails();
    var properties =
        new UserDemographicsController(VistalinkProperties.builder().vistas(List.of(tampa)).build())
            .properties(
                tampa.name(),
                requirePropertyValue("standard.access-code"),
                requirePropertyValue("standard.verify-code"));
    log.info("{}", properties);
    assertThat(properties).containsKey("DUZ");
  }
}
