package gov.va.api.lighthouse.charon.service.controller;

import static gov.va.api.lighthouse.charon.service.controller.NameResolution.noAdditionalCandidates;

import gov.va.api.health.autoconfig.logging.Redact;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.config.VistalinkProperties;
import gov.va.api.lighthouse.charon.service.controller.VistaLinkExceptions.NameResolutionException;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(
    path = "/internal/user-demographics",
    produces = {"application/json"})
@AllArgsConstructor(onConstructor_ = @Autowired)
public class UserDemographicsController {

  private final VistalinkProperties vistalinkProperties;

  /**
   * Return a collection of user demographic properties determined for a access code/verify code
   * pair at a particular site.
   */
  @GetMapping(path = "/{site}")
  public Map<String, String> properties(
      @PathVariable(name = "site", required = true) String site,
      @Redact @NotBlank @RequestHeader(name = "accessCode", required = true) String accessCode,
      @Redact @NotBlank @RequestHeader(name = "verifyCode", required = true) String verifyCode) {

    List<ConnectionDetails> connectionDetails =
        NameResolution.builder()
            .properties(vistalinkProperties)
            .additionalCandidates(noAdditionalCandidates())
            .build()
            .resolve(RpcVistaTargets.builder().include(List.of(site)).build());
    if (connectionDetails.size() != 1) {
      throw new NameResolutionException(
          ErrorCodes.AMBIGUOUS_SITE,
          "expected 1 site, but found " + connectionDetails.size(),
          null);
    }
    try (var session =
        StandardUserVistalinkSession.builder()
            .connectionDetails(connectionDetails.get(0))
            .accessCode(accessCode)
            .verifyCode(verifyCode)
            .build()) {
      return session.userDemographics();
    }
  }

  enum ErrorCodes {
    AMBIGUOUS_SITE
  }
}
