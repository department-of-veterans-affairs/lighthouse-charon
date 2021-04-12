package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import gov.va.api.lighthouse.charon.api.RpcMetadata;
import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.ClinicalAuthorizationStatusProperties;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AuthorizationStatusControllerTest {
  @Mock ParallelRpcExecutor rpcExecutor;

  private static AuthorizationStatusController.ClinicalAuthorizationResponse expectedBodyOf(
      String status, String value) {
    return AuthorizationStatusController.ClinicalAuthorizationResponse.builder()
        .status(status)
        .value(value)
        .build();
  }

  private static Stream<Arguments> responseParsing() {
    return Stream.of(
        Arguments.of(Map.of("1000", "1^9999"), "1000", 200, expectedBodyOf("ok", "1")),
        Arguments.of(Map.of("1000", "2^8888^9999^"), "1000", 200, expectedBodyOf("ok", "2")),
        Arguments.of(Map.of("1000", "-1^9999"), "1000", 401, expectedBodyOf("unauthorized", "-1")),
        Arguments.of(
            Map.of("1000", "-9001^8888^9999^"), "1000", 403, expectedBodyOf("forbidden", "-9001")),
        Arguments.of(
            Map.of("1000", "-WORDS^9999"),
            "1000",
            500,
            expectedBodyOf("Cannot parse authorization response. -WORDS", "-WORDS")),
        Arguments.of(
            Map.of("1000", "WRONG SITE"),
            "2000",
            500,
            expectedBodyOf(
                "Mismatched station id in response. Found: [WRONG SITE], Expected: 2000", null)),
        Arguments.of(
            Map.of("1000", "WRONG SITE", "2000", "RIGHT SITE"),
            "2000",
            500,
            expectedBodyOf(
                "Multiple response sites found. Only expecting one response. Found: [RIGHT SITE, WRONG SITE]",
                null)));
  }

  @Test
  void clinicalAuthorization() {
    when(rpcExecutor.execute(
            RpcRequest.builder()
                .target(RpcVistaTargets.builder().include(List.of("SITE")).build())
                .principal(
                    RpcPrincipal.builder()
                        .applicationProxyUser("apu5555")
                        .accessCode("ac1234")
                        .verifyCode("vc9876")
                        .build())
                .rpc(
                    RpcDetails.builder()
                        .name("LHS CHECK OPTION ACCESS")
                        .context("LHS RPC CONTEXT")
                        .parameters(
                            List.of(
                                RpcDetails.Parameter.builder().string("DUZ").build(),
                                RpcDetails.Parameter.builder().string("MENUOPTION").build()))
                        .build())
                .build()))
        .thenReturn(
            RpcResponse.builder()
                .status(RpcResponse.Status.OK)
                .results(
                    List.of(
                        RpcInvocationResult.builder()
                            .response("1^1")
                            .vista("SITE")
                            .metadata(RpcMetadata.builder().timezone("America/New_York").build())
                            .build()))
                .build());
    assertThat(controller().clinicalAuthorization("SITE", "DUZ", "MENUOPTION"))
        .isEqualTo(
            ResponseEntity.status(200)
                .body(
                    AuthorizationStatusController.ClinicalAuthorizationResponse.builder()
                        .status("ok")
                        .value("1")
                        .build()));
  }

  AuthorizationStatusController controller() {
    ClinicalAuthorizationStatusProperties properties =
        ClinicalAuthorizationStatusProperties.builder()
            .accessCode("ac1234")
            .verifyCode("vc9876")
            .applicationProxyUser("apu5555")
            .defaultMenuOption("whoDis")
            .build();
    return new AuthorizationStatusController(rpcExecutor, properties);
  }

  @ParameterizedTest
  @MethodSource
  void responseParsing(
      Map<String, String> results,
      String site,
      int expectedStatus,
      AuthorizationStatusController.ClinicalAuthorizationResponse expectedBody) {
    assertThat(controller().parseLhsCheckOptionAccessResponse(results, site))
        .isEqualTo(ResponseEntity.status(expectedStatus).body(expectedBody));
  }
}
