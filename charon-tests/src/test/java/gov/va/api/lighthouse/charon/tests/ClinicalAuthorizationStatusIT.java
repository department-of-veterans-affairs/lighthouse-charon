package gov.va.api.lighthouse.charon.tests;

import static gov.va.api.lighthouse.charon.tests.TestOptions.assumeVistaIsAvailable;

import gov.va.api.health.sentinel.ExpectedResponse;
import org.junit.jupiter.api.Test;

public class ClinicalAuthorizationStatusIT {

  private ExpectedResponse request(String site, String duz) {
    return ExpectedResponse.of(
        TestClients.charon()
            .service()
            .requestSpecification()
            .log()
            .all()
            .queryParam("site", site)
            .queryParam("duz", duz)
            .get("/authorization-status/clinical"));
  }

  @Test
  void returns200orKnownUserAtKnownSiteWithPermissions() {
    assumeVistaIsAvailable();
    String site = "673";
    String duz = "1";
    var authorized = SystemDefinitions.get().authorizedClinicalUser();
    request(authorized.site(), authorized.duz()).expect(200);
  }

  @Test
  void returns400ForUnknownSite() {
    assumeVistaIsAvailable();
    var authorized = SystemDefinitions.get().authorizedClinicalUser();
    request("999", authorized.duz()).expect(400);
  }

  @Test
  void returns401ForUnknownUser() {
    assumeVistaIsAvailable();
    var authorized = SystemDefinitions.get().authorizedClinicalUser();
    request(authorized.site(), "123").expect(401);
  }
}
