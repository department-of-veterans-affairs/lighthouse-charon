package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import gov.va.api.lighthouse.vistalink.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.api.RpcResponse;
import gov.va.api.lighthouse.vistalink.api.RpcResponse.Status;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.api.lighthouse.vistalink.service.config.VistalinkProperties;
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
    assertThat(controller().connections())
        .isEqualTo(
            VistalinkProperties.builder()
                .vista(_connectionDetail(1))
                .vista(_connectionDetail(2))
                .build());
  }

  RpcController controller() {
    return new RpcController(
        executor,
        VistalinkProperties.builder()
            .vista(_connectionDetail(1))
            .vista(_connectionDetail(2))
            .build());
  }

  @Test
  void invokeReturnsRpcResponse() {
    var r = RpcResponse.builder().status(Status.OK).build();
    when(executor.execute(Mockito.any(RpcRequest.class))).thenReturn(r);
    assertThat(controller().invoke(RpcRequest.builder().build())).isEqualTo(r);
  }
}
