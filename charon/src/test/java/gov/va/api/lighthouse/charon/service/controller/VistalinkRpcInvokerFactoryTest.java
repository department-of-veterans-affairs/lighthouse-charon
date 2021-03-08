package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

public class VistalinkRpcInvokerFactoryTest {

  @Test
  void createReturnsAnRpcInvokerFactory() {
    assertThat(
            new VistalinkRpcInvokerFactory(
                new MacroProcessorFactory(
                    List.of(new FugaziMacros.AppendXMacro(), new FugaziMacros.ToUpperCaseMacro()))))
        .isInstanceOf(RpcInvokerFactory.class);
  }
}
