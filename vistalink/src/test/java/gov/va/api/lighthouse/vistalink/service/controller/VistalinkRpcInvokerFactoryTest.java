package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

public class VistalinkRpcInvokerFactoryTest {

  @Test
  void createReturnsAnRpcInvokerFactory() {
    assertThat(
            new VistalinkRpcInvokerFactory(
                new MacroProcessorFactory(List.of(new AppendXMacro(), new ToUpperCaseMacro()))))
        .isInstanceOf(RpcInvokerFactory.class);
  }
}
