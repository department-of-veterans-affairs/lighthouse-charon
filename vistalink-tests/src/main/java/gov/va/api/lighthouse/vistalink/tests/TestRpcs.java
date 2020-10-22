package gov.va.api.lighthouse.vistalink.tests;

import gov.va.api.lighthouse.vistalink.service.api.RpcDetails;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TestRpcs {
  @NotNull RpcDetails pingRpc;
}
