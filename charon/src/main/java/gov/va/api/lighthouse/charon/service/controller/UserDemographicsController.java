package gov.va.api.lighthouse.charon.service.controller;

import static gov.va.api.lighthouse.charon.service.controller.NameResolution.noAdditionalCandidates;

import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.config.VistalinkProperties;
import gov.va.api.lighthouse.charon.service.controller.VistaLinkExceptions.NameResolutionException;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.security.CallbackHandlerUnitTest;
import gov.va.med.vistalink.security.VistaKernelPrincipalImpl;
import gov.va.med.vistalink.security.m.VistaKernelPrincipal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.JAXBContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserDemographicsController {

  private static final JAXBContext JAXB_CONTEXT = createJaxbContext();

  private static final AtomicInteger LOGIN_CONTEXT_ID = new AtomicInteger(0);

  private final VistalinkProperties vistalinkProperties;

  @SneakyThrows
  private static JAXBContext createJaxbContext() {
    return JAXBContext.newInstance(VistalinkXmlResponse.class);
  }

  @GetMapping(path = "/{site}")
  public Map<String, String> properties(
      @PathVariable(name = "site", required = true) String site,
      @NotBlank @RequestHeader(name = "accessCode", required = true) String accessCode,
      @NotBlank @RequestHeader(name = "verifyCode", required = true) String verifyCode) {

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
    try (var ctx =
        UserDemographicsContext.builder()
            .connectionDetails(connectionDetails.get(0))
            .rpcPrincipal(
                RpcPrincipal.builder().accessCode(accessCode).verifyCode(verifyCode).build())
            .build()) {
      return ctx.properties();
    }
  }

  enum ErrorCodes {
    AMBIGUOUS_SITE
  }

  private static class UserDemographicsContext implements AutoCloseable {

    private static final List<String> PROPERTIES =
        List.of(
            VistaKernelPrincipal.KEY_NAME_NEWPERSON01,
            VistaKernelPrincipal.KEY_NAME_DISPLAY,
            VistaKernelPrincipal.KEY_NAME_PREFIX,
            VistaKernelPrincipal.KEY_NAME_SUFFIX,
            VistaKernelPrincipal.KEY_NAME_GIVENFIRST,
            VistaKernelPrincipal.KEY_NAME_MIDDLE,
            VistaKernelPrincipal.KEY_NAME_FAMILYLAST,
            VistaKernelPrincipal.KEY_NAME_DEGREE,
            VistaKernelPrincipal.KEY_DUZ,
            VistaKernelPrincipal.KEY_TITLE,
            VistaKernelPrincipal.KEY_SERVICE_SECTION,
            VistaKernelPrincipal.KEY_LANGUAGE,
            VistaKernelPrincipal.KEY_DIVISION_IEN,
            VistaKernelPrincipal.KEY_DIVISION_STATION_NAME,
            VistaKernelPrincipal.KEY_DIVISION_STATION_NUMBER,
            VistaKernelPrincipal.KEY_DTIME,
            VistaKernelPrincipal.KEY_VPID,
            VistaKernelPrincipal.KEY_DOMAIN_NAME);

    private final RpcPrincipal rpcPrincipal;

    private final CallbackHandler handler;

    private final LoginContext loginContext;

    private final VistaKernelPrincipalImpl kernelPrincipal;

    private final ConnectionDetails connectionDetails;

    private final VistaLinkConnection connection;

    @Builder
    UserDemographicsContext(RpcPrincipal rpcPrincipal, ConnectionDetails connectionDetails) {
      this.rpcPrincipal = rpcPrincipal;
      this.connectionDetails = connectionDetails;
      handler = createLoginCallbackHandler();
      loginContext = createLoginContext();
      kernelPrincipal = createVistaKernelPrincipal();
      connection = createConnection();
    }

    @Override
    public void close() {
      try {
        loginContext.logout();
      } catch (LoginException e) {
        log.warn("{} Failed to logout", this, e);
      }
    }

    private VistaLinkConnection createConnection() {
      return kernelPrincipal.getAuthenticatedConnection();
    }

    private CallbackHandler createLoginCallbackHandler() {
      /*
       * There are only two CallbackHandlers that will work. This one, and one for Swing applications.
       * All of the internals for working with Vistalink callbacks are _package_ protected so we
       * cannot create our own, e.g. CallbackChangeVc. Despite the "UnitTest" name, decompiled code
       * reveals that this handler is simply update the VC callback objects with access code, verify
       * code, and division IEN as appropriate. I do not see any "unit test" behavior in the handler.
       * It would have been better named "UnattendedCallbackHandler".
       */
      return new CallbackHandlerUnitTest(
          rpcPrincipal.accessCode(), rpcPrincipal.verifyCode(), connectionDetails.divisionIen());
    }

    @SneakyThrows
    private LoginContext createLoginContext() {
      Configuration jaasConfiguration =
          new Configuration() {
            @Override
            public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
              return new AppConfigurationEntry[] {
                new AppConfigurationEntry(
                    "gov.va.med.vistalink.security.VistaLoginModule",
                    LoginModuleControlFlag.REQUISITE,
                    Map.of(
                        "gov.va.med.vistalink.security.ServerAddressKey",
                        connectionDetails.host(),
                        "gov.va.med.vistalink.security.ServerPortKey",
                        String.valueOf(connectionDetails.port())))
              };
            }
          };

      return new LoginContext(
          LOGIN_CONTEXT_ID.incrementAndGet() + ":" + connectionDetails.host(),
          null,
          handler,
          jaasConfiguration);
    }

    @SneakyThrows
    private VistaKernelPrincipalImpl createVistaKernelPrincipal() {
      log.info("{} Logging in", this);
      loginContext.login();
      return VistaKernelPrincipalImpl.getKernelPrincipal(loginContext.getSubject());
    }

    public Map<String, String> properties() {
      Map<String, String> values = new HashMap<>(PROPERTIES.size());
      for (String ugly : PROPERTIES) {
        String key = ugly.replace("KEY_NAME_", "").replace("KEY_", "");
        values.put(key, kernelPrincipal.getUserDemographicValue(ugly));
      }
      return values;
    }
  }
}
