package gov.va.api.lighthouse.charon.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
public class UserDemographicsIT {

  @ParameterizedTest
  @ValueSource(strings = {"/", "/charon/"})
  void userDemographicsAreReturnedWithAtLeastDuz(String basePath) {
    assumeTrue(SystemDefinitions.get().isVistaAvailable(), "Vista is unavailable.");
    var requestPath = basePath + "internal/user-demographics/{site}";
    log.info("Running health-check for path: {}", requestPath);
    var headers = TestClients.headers();
    RpcPrincipal principal = SystemDefinitions.get().avCodePrincipal();
    headers.put("accessCode", principal.accessCode());
    headers.put("verifyCode", principal.verifyCode());
    var properties =
        TestClients.charon()
            .get(headers, requestPath, SystemDefinitions.get().vistaSite())
            .expect(200)
            .expectValid(Map.class);
    assertThat(properties.get("DUZ")).isNotNull();
  }

  @ParameterizedTest
  @ValueSource(strings = {"/", "/charon/"})
  void userDemographicsRequiresAccessVerifyCodes(String basePath) {
    assumeTrue(SystemDefinitions.get().isVistaAvailable(), "Vista is unavailable.");
    var requestPath = basePath + "internal/user-demographics/{site}";
    log.info("Running health-check for path: {}", requestPath);
    var headers = TestClients.headers();
    headers.remove("accessCode");
    headers.remove("verifyCode");
    TestClients.charon().get(headers, requestPath, SystemDefinitions.get().vistaSite()).expect(400);
  }

  @Test
  void userDemographicsRequiresAccessVerifyCodes() {

    TestClients.headers();
  }
}
