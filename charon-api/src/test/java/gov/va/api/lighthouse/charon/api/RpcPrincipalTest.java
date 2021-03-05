package gov.va.api.lighthouse.charon.api;

import static gov.va.api.lighthouse.charon.api.RoundTrip.assertRoundTrip;

import org.junit.jupiter.api.Test;

public class RpcPrincipalTest {
  @Test
  void roundTrip() {
    var sample = RpcPrincipal.builder().accessCode("ac").verifyCode("vc").build();
    assertRoundTrip(sample);
  }
}
