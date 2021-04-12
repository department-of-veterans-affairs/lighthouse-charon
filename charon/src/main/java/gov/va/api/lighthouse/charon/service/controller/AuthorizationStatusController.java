package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.ClinicalAuthorizationStatusProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
      @RequestParam(name = "site") String site,
      @RequestParam(name = "duz") String duz,
      @RequestParam(name = "menu-option", required = false) String menuOption) {
    if (menuOption == null) {
      menuOption = clinicalAuthorizationStatusProperties.getDefaultMenuOption();
    }
    List<RpcInvocationResult> results =
        rpcExecutor.execute(lhsCheckOptionAccess(site, duz, menuOption)).results();
    return parseLhsCheckOptionAccessResponse(results);
  }

  private RpcRequest lhsCheckOptionAccess(String site, String duz, String menuOption) {
    return RpcRequest.builder()
        .rpc(
            RpcDetails.builder()
                .name("LHS CHECK OPTION ACCESS")
                .context("LHS RPC CONTEXT")
                .parameters(
                    List.of(
                        RpcDetails.Parameter.builder().string(duz).build(),
                        RpcDetails.Parameter.builder().string(menuOption).build()))
                .build())
        .target(RpcVistaTargets.builder().include(List.of(site)).build())
        .principal(clinicalAuthorizationStatusProperties.principal())
        .build();
  }

  private ResponseEntity<ClinicalAuthorizationResponse> parseLhsCheckOptionAccessResponse(
      List<RpcInvocationResult> results) {
    if (results.size() != 1) {
      return ResponseEntity.status(500)
          .body(
              ClinicalAuthorizationResponse.builder()
                  .status("Multiple site responses found.")
                  .build());
    }
    /*
     * Get the first piece of an authorization response string that is in the format 12345^987654.
     * We want the piece left of the ^ to make a decision off of.
     */
    if (StringUtils.isBlank(results.get(0).response())) {
      return ResponseEntity.status(500)
          .body(ClinicalAuthorizationResponse.builder().status("Blank response.").build());
    }
    String authorizationString = results.get(0).response().split("[\\^]", -1)[0];
    int authorizationStatusPiece;
    try {
      authorizationStatusPiece = Integer.parseInt(authorizationString);
    } catch (NumberFormatException e) {
      return ResponseEntity.status(500)
          .body(
              ClinicalAuthorizationResponse.builder()
                  .status("Cannot parse authorization response. " + authorizationString)
                  .build());
    }
    /*
     * ; -1:no such user in the New Person File ; -2: User terminated or has no access code ; -3: no
     * such option in the Option File ; 0: no access found in any menu tree the user owns
     *
     * <p>Positive cases are access allowed.
     */
    if (authorizationStatusPiece == -1) {
      return ResponseEntity.status(401)
          .body(
              ClinicalAuthorizationResponse.builder()
                  .status("unauthorized")
                  .rawAuthorizationResponse(authorizationString)
                  .build());
    }
    if (authorizationStatusPiece > 0) {
      return ResponseEntity.status(200)
          .body(
              ClinicalAuthorizationResponse.builder()
                  .status("ok")
                  .rawAuthorizationResponse(authorizationString)
                  .build());
    }
    return ResponseEntity.status(403)
        .body(
            ClinicalAuthorizationResponse.builder()
                .status("ok")
                .rawAuthorizationResponse(authorizationString)
                .build());
  }
}
