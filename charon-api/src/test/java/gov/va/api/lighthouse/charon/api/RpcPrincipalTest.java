package gov.va.api.lighthouse.charon.api;

import static gov.va.api.lighthouse.charon.api.RoundTrip.assertRoundTrip;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.charon.api.RpcPrincipal.LoginType;
import org.junit.jupiter.api.Test;

public class RpcPrincipalTest {
  @Test
  void roundTrip() {
    var sample = RpcPrincipal.builder().accessCode("ac").verifyCode("vc").build();
    assertRoundTrip(sample);
  }

  @Test
  void type() {
    assertThat(RpcPrincipal.builder().accessCode("ac").verifyCode("").build().type())
        .isEqualTo(LoginType.INVALID);
    assertThat(RpcPrincipal.standardUserBuilder().accessCode("ac").verifyCode("vc").build().type())
        .isEqualTo(LoginType.STANDARD_USER);
    assertThat(
            RpcPrincipal.applicationProxyUserBuilder()
                .accessCode("ac")
                .verifyCode("vc")
                .applicationProxyUser("ap")
                .build()
                .type())
        .isEqualTo(LoginType.APPLICATION_PROXY_USER);
  }
}
