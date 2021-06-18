package gov.va.api.lighthouse.charon.api;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class RpcPrincipalsTest {
  @Test
  void loadedPrincipalsAreUseable() {
    assertThat(testPrincipals().findPrincipals("LHS CHECK OPTION ACCESS"))
        .isEqualTo(List.of(testPrincipal()));
    assertThat(testPrincipals().findPrincipals("WHO DIS?")).isNull();
    assertThat(testPrincipals().findPrincipal("LHS CHECK OPTION ACCESS", "673"))
        .isEqualTo(testPrincipal());
    assertThat(testPrincipals().findPrincipal("WHO DIS?", "WHERE DIS?")).isNull();
  }

  @Test
  void rpcPrincipalConfig() {
    assertThat(testConfig())
        .isEqualTo(
            RpcPrincipalConfig.builder()
                .rpcPrincipals(
                    List.of(
                        RpcPrincipalConfig.PrincipalEntry.builder()
                            .rpcNames(List.of("LHS CHECK OPTION ACCESS"))
                            .applicationProxyUser("FAKE APU")
                            .codes(
                                List.of(
                                    RpcPrincipalConfig.Codes.builder()
                                        .sites(List.of("673"))
                                        .accessCode("FAKE ACCESS CODE")
                                        .verifyCode("FAKE VERIFY CODE")
                                        .build()))
                            .build()))
                .build());
  }

  @SneakyThrows
  private RpcPrincipalConfig testConfig() {
    return new ObjectMapper()
        .readValue(new File("src/test/resources/principals.json"), RpcPrincipalConfig.class);
  }

  private RpcPrincipal testPrincipal() {
    return RpcPrincipal.builder()
        .accessCode("FAKE ACCESS CODE")
        .verifyCode("FAKE VERIFY CODE")
        .applicationProxyUser("FAKE APU")
        .build();
  }

  private RpcPrincipals testPrincipals() {
    return new RpcPrincipals(testConfig());
  }
}
