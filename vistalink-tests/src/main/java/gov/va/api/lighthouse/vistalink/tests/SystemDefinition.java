package gov.va.api.lighthouse.vistalink.tests;

import gov.va.api.health.sentinel.ServiceDefinition;
import gov.va.api.lighthouse.vistalink.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.api.RpcVistaTargets;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SystemDefinition {
  @NotNull ServiceDefinition vistalink;
  @NotNull TestRpcs testRpcs;
  Optional<String> clientKey;
  boolean isVistalinkAvailable;
  @NotNull RpcPrincipal testRpcPrincipal;
  @NotNull RpcVistaTargets testTargets;
}
