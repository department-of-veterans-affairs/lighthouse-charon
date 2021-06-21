package gov.va.api.lighthouse.charon.service.config;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.api.RpcPrincipalConfig;
import gov.va.api.lighthouse.charon.api.RpcPrincipals;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RpcPrincipalsLoaderTest {

  @Test
  void loadPrincipals() {
    RpcPrincipals actual =
        new RpcPrincipalsLoader().loadPrincipals("src/test/resources/principals.json");
    assertThat(actual.findPrincipals("LHS CHECK OPTION ACCESS"))
        .isEqualTo(
            List.of(
                RpcPrincipal.builder()
                    .applicationProxyUser("FAKE APU")
                    .accessCode("FAKE ACCESS CODE")
                    .verifyCode("FAKE VERIFY CODE")
                    .build()));
  }
}
