package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  /** Test a users clinical authorization status. Uses the LHS CHECK OPTION ACCESS VPC. */
  @GetMapping(
      value = {"/clinical"},
      params = {"site", "duz"})
  public RpcResponse clinicalAuthorization(
      @RequestParam(name = "site") String site,
      @RequestParam(name = "duz") String duz,
      @Value("${charon.authorization-status.application-proxy-user}") String applicationProxyUser,
      @Value("${charon.authorization-status.access-code}") String accessCode,
      @Value("${charon.authorization-status.verify-code}") String verifyCode) {
    return rpcExecutor.execute(
        RpcRequest.builder()
            .rpc(
                RpcDetails.builder()
                    .name("LHS CHECK OPTION ACCESS")
                    .context("LHS RPC CONTEXT")
                    .parameters(
                        List.of(
                            RpcDetails.Parameter.builder().string(duz).build(),
                            RpcDetails.Parameter.builder().string("OR CPRS GUI CHART").build()))
                    .build())
            .target(RpcVistaTargets.builder().include(List.of(site)).build())
            .principal(
                RpcPrincipal.builder()
                    .applicationProxyUser(applicationProxyUser)
                    .accessCode(accessCode)
                    .verifyCode(verifyCode)
                    .build())
            .build());
  }
}
