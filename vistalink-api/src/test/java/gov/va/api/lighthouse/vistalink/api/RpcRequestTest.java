package gov.va.api.lighthouse.vistalink.api;

import static gov.va.api.lighthouse.vistalink.api.RoundTrip.assertRoundTrip;

import java.util.List;
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
            .rpc(RpcDetails.builder().name("FAUX NAME").context("FAUX CONTEXT").build())
            .build();
    assertRoundTrip(sample);
  }
}
