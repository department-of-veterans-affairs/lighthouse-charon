package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.models.lhscheckoptionaccess.LhsCheckOptionAccess;
import gov.va.api.lighthouse.charon.service.config.ClinicalAuthorizationStatusProperties;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;

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

import static gov.va.api.lighthouse.charon.service.controller.AlternateAuthorizationStatusIds.*;

@Validated
@RestController()
@RequestMapping(
        path = "/authorization-status",
        produces = {"application/json"})
@AllArgsConstructor(onConstructor_ = @Autowired)
public class AuthorizationStatusController {
    private final RpcExecutor rpcExecutor;

    private final ClinicalAuthorizationStatusProperties clinicalAuthorizationStatusProperties;

    private final AlternateAuthorizationStatusIds alternateAuthorizationStatusIds;

    /**
     * Test a users clinical authorization status. Uses the LHS CHECK OPTION ACCESS VPC.
     */
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
        AuthorizationId authorizationId = alternateAuthorizationStatusIds.toPrivateId(AuthorizationId.builder()
                .duz(duz)
                .site(site)
                .build());
        RpcResponse response =
                rpcExecutor.execute(
                        RpcRequest.builder()
                                .rpc(
                                        LhsCheckOptionAccess.Request.builder()
                                                .duz(authorizationId.duz())
                                                .menuOption(menuOption)
                                                .build()
                                                .asDetails())
                                .target(RpcVistaTargets.builder().include(List.of(authorizationId.site())).build())
                                .principal(clinicalAuthorizationStatusProperties.principal())
                                .build());
        LhsCheckOptionAccess.Response typeSafeResult =
                LhsCheckOptionAccess.create().fromResults(response.results());
        return parseLhsCheckOptionAccessResponse(typeSafeResult.resultsByStation(), site);
    }

    ResponseEntity<ClinicalAuthorizationResponse> parseLhsCheckOptionAccessResponse(
            Map<String, String> results, String site) {
        if (results.size() != 1) {
            return responseOf(
                    "Only expecting one result from site: " + site,
                    results.values().stream().sorted().collect(Collectors.joining(", ")),
                    500);
        }
        if (!results.containsKey(site)) {
            return responseOf(
                    "Response missing for site: " + site, String.join(", ", results.values()), 500);
        }
        if (StringUtils.isBlank(results.get(site))) {
            return responseOf("Blank response from vista", null, 500);
        }
        String authorizationString = results.get(site).split("[\\^]", -1)[0];
        int authorizationStatusPiece;
        try {
            authorizationStatusPiece = Integer.parseInt(authorizationString);
        } catch (NumberFormatException e) {
            return responseOf("Cannot parse authorization response.", authorizationString, 500);
        }
        /*
         *  -1: no such user in the New Person File.
         *  -2: User terminated or has no access code.
         *  -3: no such option in the Option File.
         *  0: no access found in any menu tree the user owns.
         *  Positive cases are access allowed.
         */
        if (authorizationStatusPiece > 0) {
            return responseOf("ok", authorizationString, 200);
        }
        if (authorizationStatusPiece == -1) {
            return responseOf("unauthorized", authorizationString, 401);
        }
        return responseOf("forbidden", authorizationString, 403);
    }

    private ResponseEntity<ClinicalAuthorizationResponse> responseOf(
            String status, String value, int httpCode) {
        return ClinicalAuthorizationResponse.builder()
                .status(status)
                .value(value)
                .build()
                .response(httpCode);
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
