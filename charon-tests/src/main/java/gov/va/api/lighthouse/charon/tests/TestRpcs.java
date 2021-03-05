package gov.va.api.lighthouse.charon.tests;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TestRpcs {
  @NotNull RpcDetails pingRpc;
  @NotNull RpcDetails stringRequestRpc;
  @NotNull RpcDetails localArrayRequestRpc;
  @NotNull RpcDetails globalArrayRequestRpc;
}
