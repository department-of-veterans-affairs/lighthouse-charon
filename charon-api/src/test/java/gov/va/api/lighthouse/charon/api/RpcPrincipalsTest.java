package gov.va.api.lighthouse.charon.api;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class RpcPrincipalsTest {
  private List<RpcPrincipal> asian() {
    return List.of(
        RpcPrincipal.builder()
            .applicationProxyUser("ASIAN!")
            .accessCode("ASIAN_FOOD")
            .verifyCode("IS_AMAZING")
            .build(),
        RpcPrincipal.builder()
            .applicationProxyUser("ASIAN!")
            .accessCode("ASIAN_FOOD")
            .verifyCode("IS_STILL_GREAT")
            .build());
  }

  @Test
  void findAllPrincipalsByName() {
    assertThat(testPrincipals().findAllPrincipalsByName("TACO")).isEqualTo(mexican());
    assertThat(testPrincipals().findAllPrincipalsByName("NACHO")).isEqualTo(mexican());
    assertThat(testPrincipals().findAllPrincipalsByName("NACHO")).isEqualTo(mexican());
    assertThat(testPrincipals().findAllPrincipalsByName("STIR-FRY")).isEqualTo(asian());
    assertThat(testPrincipals().findAllPrincipalsByName("SASHIMI")).isEqualTo(asian());
    assertThat(testPrincipals().findAllPrincipalsByName("PIZZA")).isEqualTo(italian());
    assertThat(testPrincipals().findAllPrincipalsByName("SPAGHETTI")).isEqualTo(italian());
    assertThat(testPrincipals().findAllPrincipalsByName("BRUSSELSPROUTS"))
        .isEqualTo(Collections.emptyList());
    assertThat(testPrincipals().findAllPrincipalsByName(null)).isEqualTo(Collections.emptyList());
  }

  @Test
  void findPrincipalByNameAndSite() {
    assertThat(testPrincipals().findPrincipalByNameAndSite("TACO", "111"))
        .isEqualTo(
            Optional.of(
                RpcPrincipal.builder()
                    .applicationProxyUser("MEXICAN!")
                    .accessCode("MEXICAN_FOOD")
                    .verifyCode("IS_AMAZING")
                    .build()));
    assertThat(testPrincipals().findPrincipalByNameAndSite("TACO", "111-M"))
        .isEqualTo(
            Optional.of(
                RpcPrincipal.builder()
                    .applicationProxyUser("MEXICAN!")
                    .accessCode("MEXICAN_FOOD")
                    .verifyCode("IS_STILL_GREAT")
                    .build()));
    assertThat(testPrincipals().findPrincipalByNameAndSite("SASHIMI", "222-A"))
        .isEqualTo(
            Optional.of(
                RpcPrincipal.builder()
                    .applicationProxyUser("ASIAN!")
                    .accessCode("ASIAN_FOOD")
                    .verifyCode("IS_STILL_GREAT")
                    .build()));
    assertThat(testPrincipals().findPrincipalByNameAndSite("PIZZA", "666"))
        .isEqualTo(
            Optional.of(
                RpcPrincipal.builder()
                    .applicationProxyUser("ITALIAN!")
                    .accessCode("ITALIAN_FOOD")
                    .verifyCode("IS_AMAZING")
                    .build()));
    assertThat(testPrincipals().findPrincipalByNameAndSite("CHEESECAKE", "NOPE"))
        .isEqualTo(Optional.empty());
    assertThat(testPrincipals().findPrincipalByNameAndSite("CHEESECAKE", "111-A"))
        .isEqualTo(Optional.empty());
    assertThat(testPrincipals().findPrincipalByNameAndSite("PIZZA", "111-M"))
        .isEqualTo(Optional.empty());
    assertThat(testPrincipals().findPrincipalByNameAndSite(null, null)).isEqualTo(Optional.empty());
  }

  @Test
  void isRpcNamesUnique() {
    assertThat(
            RpcPrincipals.builder()
                .entries(
                    List.of(
                        RpcPrincipals.PrincipalEntry.builder()
                            .rpcNames(List.of("TACO", "SUSHI", "BEER"))
                            .build(),
                        RpcPrincipals.PrincipalEntry.builder()
                            .rpcNames(List.of("STRAWBERRY", "CURRY", "WINE"))
                            .build()))
                .build()
                .isRpcNamesUnique())
        .isTrue();
    assertThat(
            RpcPrincipals.builder()
                .entries(
                    List.of(
                        RpcPrincipals.PrincipalEntry.builder()
                            .rpcNames(List.of("TACO", "SUSHI", "BEER"))
                            .build(),
                        RpcPrincipals.PrincipalEntry.builder()
                            .rpcNames(List.of("STRAWBERRY", "CURRY", "WINE", "TACO"))
                            .build()))
                .build()
                .isRpcNamesUnique())
        .isFalse();
  }

  @Test
  void isSiteUniqueWithinCodes() {
    assertThat(
            RpcPrincipals.PrincipalEntry.builder()
                .codes(
                    List.of(
                        RpcPrincipals.Codes.builder()
                            .sites(List.of("TACO", "SUSHI", "BEER"))
                            .build(),
                        RpcPrincipals.Codes.builder()
                            .sites(List.of("STRAWBERRY", "CURRY", "WINE"))
                            .build()))
                .build()
                .isSitesUniqueWithinCodes())
        .isTrue();
    assertThat(
            RpcPrincipals.PrincipalEntry.builder()
                .codes(
                    List.of(
                        RpcPrincipals.Codes.builder()
                            .sites(List.of("TACO", "SUSHI", "BEER"))
                            .build(),
                        RpcPrincipals.Codes.builder()
                            .sites(List.of("STRAWBERRY", "CURRY", "WINE", "TACO"))
                            .build()))
                .build()
                .isSitesUniqueWithinCodes())
        .isFalse();
  }

  private List<RpcPrincipal> italian() {
    return List.of(
        RpcPrincipal.builder()
            .applicationProxyUser("ITALIAN!")
            .accessCode("ITALIAN_FOOD")
            .verifyCode("IS_AMAZING")
            .build());
  }

  private List<RpcPrincipal> mexican() {
    return List.of(
        RpcPrincipal.builder()
            .applicationProxyUser("MEXICAN!")
            .accessCode("MEXICAN_FOOD")
            .verifyCode("IS_AMAZING")
            .build(),
        RpcPrincipal.builder()
            .applicationProxyUser("MEXICAN!")
            .accessCode("MEXICAN_FOOD")
            .verifyCode("IS_STILL_GREAT")
            .build());
  }

  @SneakyThrows
  private RpcPrincipals testConfig() {
    return new ObjectMapper()
        .readValue(new File("src/test/resources/principals.json"), RpcPrincipals.class);
  }

  private RpcPrincipalLookup testPrincipals() {
    return new RpcPrincipalLookup(testConfig());
  }
}
