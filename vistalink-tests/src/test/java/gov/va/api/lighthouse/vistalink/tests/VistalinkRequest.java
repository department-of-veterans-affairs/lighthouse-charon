package gov.va.api.lighthouse.vistalink.tests;

import gov.va.api.health.sentinel.ExpectedResponse;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
class VistalinkRequest {
  private final String env = System.getProperty("sentinel");

  static Boolean isEnvironmentReady() {
    if (StringUtils.equals(env, "LOCAL")
        && BooleanUtils.toBoolean(System.getProperty("test.vistalink"))) {
      return true;
    } else if (StringUtils.equals(env, "QA")) {
      return true;
    }
    return false;
  }

  static ExpectedResponse request(Object body) {
    return TestClients.vistalink().post(Map.of("Content-Type", "application/json"), "rpc", body);
  }
}
