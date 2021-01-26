package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DfnMacroTest {

  @Mock MacroExecutionContext executionContext;

  @Test
  void checkDfnMacro() {
    var dfn = new DfnMacro();
    RpcResponse response = new FugaziRpcResponse("mydfn");
    when(executionContext.invoke(any(RpcRequest.class))).thenReturn(response);
    assertThat(dfn.name()).isEqualTo("dfn"); // Coverage requirement, will remove
    assertThat(dfn.evaluate(executionContext, "myicn")).isEqualTo("mydfn");
  }

  static class FugaziRpcResponse extends RpcResponse {
    FugaziRpcResponse(String value) {
      super(null, null, null, null, value, null);
    }
  }
}
