package gov.va.api.lighthouse.charon.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UserDemographicsIT {

  @Test
  void userDemographicsAreReturnedWithAtLeastDuz() {
    assumeTrue(SystemDefinitions.get().isVistaAvailable(), "Vista is unavailable.");
    var requestPath =
        SystemDefinitions.get().charon().apiPath() + "internal/user-demographics/{site}";
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

  @Test
  void userDemographicsRequiresAccessVerifyCodes() {
    assumeTrue(SystemDefinitions.get().isVistaAvailable(), "Vista is unavailable.");
    var requestPath =
        SystemDefinitions.get().charon().apiPath() + "internal/user-demographics/{site}";
    var headers = TestClients.headers();
    headers.remove("accessCode");
    headers.remove("verifyCode");
    TestClients.charon().get(headers, requestPath, SystemDefinitions.get().vistaSite()).expect(400);
  }
}
