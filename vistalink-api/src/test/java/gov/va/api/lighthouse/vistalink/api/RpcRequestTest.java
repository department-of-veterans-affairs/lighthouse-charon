package gov.va.api.lighthouse.vistalink.api;

import static gov.va.api.lighthouse.vistalink.api.RoundTrip.assertRoundTrip;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class RpcRequestTest {
  @Test
  void roundTrip() {
    var sample =
        RpcRequest.builder()
            .principal(RpcPrincipal.builder().accessCode("ac").verifyCode("vc").build())
            .target(
                RpcVistaTargets.builder()
                    .forPatient("p1")
                    .include(List.of("1"))
                    .exclude(List.of("2"))
                    .build())
            .rpc(
                RpcDetails.builder()
                    .name("FAUX NAME")
                    .context("FAUX CONTEXT")
                    .parameters(
                        List.of(
                            RpcDetails.Parameter.builder().string("").build(),
                            RpcDetails.Parameter.builder().string("a").build(),
                            RpcDetails.Parameter.builder().ref("").build(),
                            RpcDetails.Parameter.builder().ref("b").build(),
                            RpcDetails.Parameter.builder().array(List.of()).build(),
                            RpcDetails.Parameter.builder().array(List.of("c")).build(),
                            RpcDetails.Parameter.builder().namedArray(Map.of()).build(),
                            RpcDetails.Parameter.builder().namedArray(Map.of("d", "e")).build()))
                    .build())
            .build();
    assertRoundTrip(sample);
  }
}
