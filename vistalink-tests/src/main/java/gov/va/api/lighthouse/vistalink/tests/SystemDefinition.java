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
}
