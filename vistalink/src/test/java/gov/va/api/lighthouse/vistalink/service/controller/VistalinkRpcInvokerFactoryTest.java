package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class VistalinkRpcInvokerFactoryTest {

  @Test
  void createReturnsAnRpcInvokerFactory() {
    assertThat(new VistalinkRpcInvokerFactory()).isInstanceOf(RpcInvokerFactory.class);
  }
}
