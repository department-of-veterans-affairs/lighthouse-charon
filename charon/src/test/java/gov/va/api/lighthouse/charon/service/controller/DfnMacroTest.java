package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
  void evaluateReturnValueForUnknownIcn() {
    var dfn = new DfnMacro();
    RpcResponse response = new FugaziRpcResponse("-1^ICN NOT IN DATABASE");
    when(executionContext.invoke(any(RpcRequest.class))).thenReturn(response);
    assertThatExceptionOfType(DfnMacro.IcnNotFound.class)
        .isThrownBy(() -> dfn.evaluate(executionContext, "badicn"));
  }

  @Test
  void evaluateReturnsDfnForKnownIcn() {
    var dfn = new DfnMacro();
    RpcResponse response = new FugaziRpcResponse("mydfn");
    when(executionContext.invoke(any(RpcRequest.class))).thenReturn(response);
    assertThat(dfn.evaluate(executionContext, "myicn")).isEqualTo("mydfn");
  }

  @Test
  void nameIsDfn() {
    assertThat(new DfnMacro().name()).isEqualTo("dfn");
  }

  static class FugaziRpcResponse extends RpcResponse {
    FugaziRpcResponse(String value) {
      super(null, null, null, null, value, null);
    }
  }
}
