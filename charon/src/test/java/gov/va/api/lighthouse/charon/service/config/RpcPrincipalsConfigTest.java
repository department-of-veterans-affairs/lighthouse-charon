package gov.va.api.lighthouse.charon.service.config;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.api.RpcPrincipalLookup;
import gov.va.api.lighthouse.charon.api.RpcPrincipals;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class RpcPrincipalsConfigTest {

  @Test
  void loadPrincipals() {
    RpcPrincipalLookup testPrincipals =
        new RpcPrincipalConfig().loadPrincipals("src/test/resources/principals.json");
    assertThat(testPrincipals.findPrincipalByNameAndSite("SASHIMI", "222-A"))
        .isEqualTo(
            Optional.of(
                RpcPrincipal.builder()
                    .applicationProxyUser("ASIAN!")
                    .accessCode("ASIAN_FOOD")
                    .verifyCode("IS_STILL_GREAT")
                    .build()));
  }

  @Test
  void validate() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(
            () -> new RpcPrincipalConfig().validate(RpcPrincipals.builder().build(), "whatever"));
  }
}
