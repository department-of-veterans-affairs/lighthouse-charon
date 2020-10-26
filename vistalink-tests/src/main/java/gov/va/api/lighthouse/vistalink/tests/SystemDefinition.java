package gov.va.api.lighthouse.vistalink.tests;

import gov.va.api.health.sentinel.ServiceDefinition;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SystemDefinition {
  @NotNull ServiceDefinition vistalink;
  @NotNull TestRpcs testRpcs;
  @NotNull Boolean isVistalinkAvailable;

  public String vistaAccessCode() {
    return System.getProperty("vista.access-code", "not-set");
  }

  public String vistaVerifyCode() {
    return System.getProperty("vista.verify-code", "not-set");
  }
}
