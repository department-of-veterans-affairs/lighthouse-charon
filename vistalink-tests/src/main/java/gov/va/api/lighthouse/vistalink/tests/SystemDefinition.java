package gov.va.api.lighthouse.vistalink.tests;

import gov.va.api.health.sentinel.ServiceDefinition;
import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SystemDefinition {
  @NotNull ServiceDefinition vistalink;
  @NotNull TestRpcs testRpcs;
  @NotNull Boolean isVistalinkAvailable;
  @NotNull RpcPrincipal testRpcPrincipal;
}
