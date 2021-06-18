package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import gov.va.api.lighthouse.charon.api.RpcResponse.Status;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.config.EncyptedLoggingConfig.DisabledEncryptedLogging;
import gov.va.api.lighthouse.charon.service.config.VistalinkProperties;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RpcControllerTest {

  @Mock RpcExecutor executor;

  private ConnectionDetails _connectionDetail(int n) {
    return ConnectionDetails.builder()
        .name("v" + n)
        .host("v" + n + ".com")
        .port(8000 + n)
        .divisionIen("" + n)
        .build();
  }

  @Test
  void connectionsReturnsConnectionDetails() {
    Assertions.assertThat(controller().connections())
        .isEqualTo(
            VistalinkProperties.builder()
                .vistas(List.of(_connectionDetail(1), _connectionDetail(2)))
                .build());
  }

  RpcController controller() {
    return new RpcController(
        executor,
        VistalinkProperties.builder()
            .vistas(List.of(_connectionDetail(1), _connectionDetail(2)))
            .build(),
        new DisabledEncryptedLogging());
  }

  @Test
  void invokeReturnsRpcResponse() {
    var r = RpcResponse.builder().status(Status.OK).build();
    when(executor.execute(Mockito.any(RpcRequest.class))).thenReturn(r);
    assertThat(controller().invoke(RpcRequest.builder().build())).isEqualTo(r);
  }
}
