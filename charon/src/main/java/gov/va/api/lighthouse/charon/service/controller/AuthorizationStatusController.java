package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.models.lhscheckoptionaccess.LhsCheckOptionAccess;
import gov.va.api.lighthouse.charon.service.config.ClinicalAuthorizationStatusProperties;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Validated
@RestController()
@RequestMapping(
    path = "/authorization-status",
    produces = {"application/json"})
@AllArgsConstructor(onConstructor_ = @Autowired)
public class AuthorizationStatusController {
  private final RpcExecutor rpcExecutor;

  private final ClinicalAuthorizationStatusProperties clinicalAuthorizationStatusProperties;

  /** Test a users clinical authorization status. Uses the LHS CHECK OPTION ACCESS VPC. */
  @GetMapping(
      value = {"/clinical"},
      params = {"site", "duz"})
  public ResponseEntity<ClinicalAuthorizationResponse> clinicalAuthorization(
      @NotBlank @RequestParam(name = "site") String site,
      @NotBlank @RequestParam(name = "duz") String duz,
      @RequestParam(name = "menu-option", required = false) String menuOption) {
    if (StringUtils.isBlank(menuOption)) {
      menuOption = clinicalAuthorizationStatusProperties.getDefaultMenuOption();
    }
    RpcResponse response =
        rpcExecutor.execute(
            RpcRequest.builder()
                .rpc(
                    LhsCheckOptionAccess.Request.builder()
                        .duz(duz)
                        .menuOption(menuOption)
                        .build()
                        .asDetails())
                .target(RpcVistaTargets.builder().include(List.of(site)).build())
                .principal(clinicalAuthorizationStatusProperties.principal())
                .build());
    LhsCheckOptionAccess.Response typeSafeResult =
        LhsCheckOptionAccess.create().fromResults(response.results());
    return parseLhsCheckOptionAccessResponse(typeSafeResult.resultsByStation().orElseThrow(), site);
  }

  ResponseEntity<ClinicalAuthorizationResponse> parseLhsCheckOptionAccessResponse(
      Map<String, String> results, String site) {
    if (results.size() != 1) {
      return ClinicalAuthorizationResponse.builder()
          .status("Multiple response sites found. Only expecting one response.")
          .value(results.values().stream().sorted().collect(Collectors.joining(", ")))
          .build()
          .response(500);
    }
    if (!results.containsKey(site)) {
      return ClinicalAuthorizationResponse.builder()
          .status("Mismatched station id in response.")
          .value(String.join(", ", results.values()))
          .build()
          .response(500);
    }
    if (StringUtils.isBlank(results.get(site))) {
      return ClinicalAuthorizationResponse.builder()
          .status("Blank response from vista.")
          .build()
          .response(500);
    }
    String authorizationString = results.get(site).split("[\\^]", -1)[0];
    int authorizationStatusPiece;
    try {
      authorizationStatusPiece = Integer.parseInt(authorizationString);
    } catch (NumberFormatException e) {
      return ClinicalAuthorizationResponse.builder()
          .status("Cannot parse authorization response.")
          .value(authorizationString)
          .build()
          .response(500);
    }
    /*
     *  -1: no such user in the New Person File.
     *  -2: User terminated or has no access code.
     *  -3: no such option in the Option File.
     *  0: no access found in any menu tree the user owns.
     *  Positive cases are access allowed.
     */
    if (authorizationStatusPiece == -1) {
      return ClinicalAuthorizationResponse.builder()
          .status("unauthorized")
          .value(authorizationString)
          .build()
          .response(401);
    }
    if (authorizationStatusPiece > 0) {
      return ClinicalAuthorizationResponse.builder()
          .status("ok")
          .value(authorizationString)
          .build()
          .response(200);
    }
    return ClinicalAuthorizationResponse.builder()
        .status("forbidden")
        .value(authorizationString)
        .build()
        .response(403);
  }

  @Value
  @Builder
  public static class ClinicalAuthorizationResponse {
    String status;
    String value;

    ResponseEntity<ClinicalAuthorizationResponse> response(int httpCode) {
      return ResponseEntity.status(httpCode).body(this);
    }
  }
}
