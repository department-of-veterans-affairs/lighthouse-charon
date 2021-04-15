package gov.va.api.lighthouse.charon.tests;

import gov.va.api.health.sentinel.ServiceDefinition;
import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.tests.SystemDefinitions.SiteDuzPair;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SystemDefinition {
  @NotNull ServiceDefinition charon;
  @NotNull TestRpcs testRpcs;
  Optional<String> clientKey;
  boolean isVistaAvailable;
  @NotNull RpcPrincipal testRpcPrincipal;
  @NotNull RpcVistaTargets testTargets;
  @NotNull SiteDuzPair authorizedClinicalUser;
}
