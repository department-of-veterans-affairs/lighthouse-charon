package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import gov.va.api.lighthouse.charon.api.RpcMetadata;
import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.api.RpcPrincipalLookup;
import gov.va.api.lighthouse.charon.api.RpcPrincipals;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.AuthorizationId;
import gov.va.api.lighthouse.charon.service.config.EncyptedLoggingConfig.DisabledEncryptedLogging;
import gov.va.api.lighthouse.charon.service.controller.AlternateAuthorizationStatusIds.AlternateAuthorizationStatusIdsDisabled;
import gov.va.api.lighthouse.charon.service.controller.AlternateAuthorizationStatusIds.AlternateAuthorizationStatusIdsEnabled;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        arguments(Map.of("1000", "1^9999"), "1000", 200, expectedBodyOf("ok", "1")),
        arguments(Map.of("1000", "2^8888^9999^"), "1000", 200, expectedBodyOf("ok", "2")),
        arguments(Map.of("1000", "-1^9999"), "1000", 401, expectedBodyOf("unauthorized", "-1")),
        arguments(
            Map.of("1000", "-9001^8888^9999^"), "1000", 403, expectedBodyOf("forbidden", "-9001")),
        arguments(
            Map.of("1000", "-WORDS^9999"),
            "1000",
            500,
            expectedBodyOf("Cannot parse authorization response.", "-WORDS")),
        arguments(
            Map.of("1000", "WRONG SITE"),
            "2000",
            500,
            expectedBodyOf("Response missing for site: 2000", "WRONG SITE")),
        arguments(
            Map.of("1000", "WRONG SITE", "2000", "RIGHT SITE"),
            "2000",
            500,
            expectedBodyOf("Only expecting one result from site: 2000", "RIGHT SITE, WRONG SITE")));
  }

  @Test
  void clinicalAuthorization() {
    when(rpcExecutor.execute(
            rpcRequest(
                List.of("publicSite1"),
                rpcPrincipal("apu5555", "ac1234", "vc9876"),
                rpcDetails("LHS CHECK OPTION ACCESS", "LHS RPC CONTEXT", "DUZ", "MENUOPTION"))))
        .thenReturn(
            rpcResponse(
                RpcResponse.Status.OK, List.of(rpcInvocationResult("1^11", "publicSite1"))));
    assertThat(controller().clinicalAuthorization("publicSite1", "DUZ", "MENUOPTION"))
        .isEqualTo(responseOf(200, "ok", "1"));
    when(rpcExecutor.execute(
            rpcRequest(
                List.of("publicSite2"),
                rpcPrincipal("apu5555", "ac1234", "vc9876"),
                rpcDetails("LHS CHECK OPTION ACCESS", "LHS RPC CONTEXT", "DUZ", "defaultOption"))))
        .thenReturn(
            rpcResponse(
                RpcResponse.Status.OK, List.of(rpcInvocationResult("1^11", "publicSite2"))));
    assertThat(controller().clinicalAuthorization("publicSite2", "DUZ", null))
        .isEqualTo(responseOf(200, "ok", "1"));
    assertThat(controller().clinicalAuthorization("publicSite2", "DUZ", ""))
        .isEqualTo(responseOf(200, "ok", "1"));
    assertThat(controller().clinicalAuthorization("unknownSite", "DUZ", ""))
        .isEqualTo(
            ResponseEntity.status(500)
                .body(
                    AuthorizationStatusController.ClinicalAuthorizationResponse.builder()
                        .status("No credentials for site.")
                        .value("unknownSite")
                        .build()));
  }

  @Test
  void clinicalAuthorizationWithAlternateIds() {
    when(rpcExecutor.execute(
            rpcRequest(
                List.of("privateSite1"),
                rpcPrincipal("apu5555", "ac1234", "vc9876"),
                rpcDetails(
                    "LHS CHECK OPTION ACCESS", "LHS RPC CONTEXT", "privateDuz1", "MENUOPTION"))))
        .thenReturn(
            rpcResponse(
                RpcResponse.Status.OK, List.of(rpcInvocationResult("1^11", "privateSite1"))));
    assertThat(
            controllerWithAlternateIds()
                .clinicalAuthorization("publicSite1", "publicDuz1", "MENUOPTION"))
        .isEqualTo(responseOf(200, "ok", "1"));
    when(rpcExecutor.execute(
            rpcRequest(
                List.of("privateSite2"),
                rpcPrincipal("apu5555", "ac1234", "vc9876"),
                rpcDetails(
                    "LHS CHECK OPTION ACCESS", "LHS RPC CONTEXT", "privateDuz2", "defaultOption"))))
        .thenReturn(
            rpcResponse(
                RpcResponse.Status.OK, List.of(rpcInvocationResult("2^11", "privateSite2"))));
    assertThat(
            controllerWithAlternateIds().clinicalAuthorization("publicSite2", "publicDuz2", null))
        .isEqualTo(responseOf(200, "ok", "2"));
  }

  AuthorizationStatusController controller() {
    return controller(new AlternateAuthorizationStatusIdsDisabled());
  }

  private AuthorizationStatusController controller(
      AlternateAuthorizationStatusIds alternateAuthorizationStatusIds) {
    return new AuthorizationStatusController(
        rpcExecutor,
        alternateAuthorizationStatusIds,
        new DisabledEncryptedLogging(),
        RpcPrincipalLookup.of(
            RpcPrincipals.builder()
                .entries(
                    List.of(
                        RpcPrincipals.PrincipalEntry.builder()
                            .rpcNames(List.of("LHS CHECK OPTION ACCESS"))
                            .applicationProxyUser("apu5555")
                            .codes(
                                List.of(
                                    RpcPrincipals.Codes.builder()
                                        .sites(List.of("publicSite1", "publicSite2"))
                                        .accessCode("ac1234")
                                        .verifyCode("vc9876")
                                        .build()))
                            .build()))
                .build()),
        "defaultOption");
  }

  AuthorizationStatusController controllerWithAlternateIds() {
    return controller(
        new AlternateAuthorizationStatusIdsEnabled(
            Map.of(
                AuthorizationId.of("publicDuz1@publicSite1"),
                AuthorizationId.of("privateDuz1@privateSite1"),
                AuthorizationId.of("publicDuz2@publicSite2"),
                AuthorizationId.of("privateDuz2@privateSite2"))));
  }

  ResponseEntity<AuthorizationStatusController.ClinicalAuthorizationResponse> responseOf(
      int httpCode, String status, String value) {
    return ResponseEntity.status(httpCode)
        .body(
            AuthorizationStatusController.ClinicalAuthorizationResponse.builder()
                .status(status)
                .value(value)
                .build());
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

  RpcDetails rpcDetails(String name, String context, String... parameters) {
    return RpcDetails.builder()
        .name(name)
        .context(context)
        .parameters(
            Arrays.stream(parameters)
                .map(p -> RpcDetails.Parameter.builder().string(p).build())
                .collect(Collectors.toList()))
        .build();
  }

  RpcInvocationResult rpcInvocationResult(String responseValue, String site) {
    return RpcInvocationResult.builder()
        .response(responseValue)
        .vista(site)
        .metadata(RpcMetadata.builder().timezone("America/New_York").build())
        .build();
  }

  RpcPrincipal rpcPrincipal(String applicationProxyUser, String accessCode, String verifyCode) {
    return RpcPrincipal.builder()
        .applicationProxyUser(applicationProxyUser)
        .accessCode(accessCode)
        .verifyCode(verifyCode)
        .build();
  }

  RpcRequest rpcRequest(List<String> sites, RpcPrincipal principal, RpcDetails details) {
    return RpcRequest.builder()
        .target(RpcVistaTargets.builder().include(sites).build())
        .principal(principal)
        .rpc(details)
        .build();
  }

  RpcResponse rpcResponse(RpcResponse.Status status, List<RpcInvocationResult> results) {
    return RpcResponse.builder().status(status).results(results).build();
  }
}
