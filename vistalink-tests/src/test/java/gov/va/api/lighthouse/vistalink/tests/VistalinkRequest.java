package gov.va.api.lighthouse.vistalink.tests;

import gov.va.api.health.sentinel.ExpectedResponse;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
class VistalinkRequest {
  private final String env = System.getProperty("sentinel");

  private static ExpectedResponse request(Object body) {
    return TestClients.vistalink().post(Map.of("Content-Type", "application/json"), "rpc", body);
  }

  static ExpectedResponse vistalinkRequest(Object body) {
    if (StringUtils.equals(env, "LOCAL")
        && BooleanUtils.toBoolean(System.getProperty("test.vistalink"))) {
      return request(body);
    } else if (StringUtils.equals(env, "QA")) {
      return request(body);
    }
    return null;
  }
}
