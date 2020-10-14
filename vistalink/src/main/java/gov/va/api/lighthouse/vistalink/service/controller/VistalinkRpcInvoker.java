package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.service.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.security.CallbackHandlerUnitTest;
import gov.va.med.vistalink.security.VistaKernelPrincipalImpl;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VistalinkRpcInvoker implements RpcInvoker {

  private final RpcPrincipal rpcPrincipal;
  private final ConnectionDetails connectionDetails;

  @Builder
  VistalinkRpcInvoker(RpcPrincipal rpcPrincipal, ConnectionDetails connectionDetails) {
    this.rpcPrincipal = rpcPrincipal;
    this.connectionDetails = connectionDetails;
  }



// -----------------------------------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------------------------------


  @Getter(lazy = true, value = AccessLevel.PRIVATE)
  private final CallbackHandler handler = createLoginCallbackHandler();

  @Getter(lazy = true, value = AccessLevel.PRIVATE)
  private final LoginContext loginContext = createLoginContext();

  @Getter(lazy = true, value = AccessLevel.PRIVATE)
  private final VistaKernelPrincipalImpl principal = createVistaKernelPrincipal();

  @Getter(lazy = true, value = AccessLevel.PRIVATE)
  private final VistaLinkConnection connection = createConnection();


  private VistaLinkConnection createConnection() {
    return principal.getAuthenticatedConnection();
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
        rpcPrincipal, config.verifyCode(), config.divisionIen());
  }
  @SneakyThrows
  private LoginContext createLoginContext() {
    Configuration jaasConfiguration =
        new Configuration() {
          @Override
          public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
            return new AppConfigurationEntry[] {
                new AppConfigurationEntry(
                    "gov.va.med.vistalink.security.ServerAddressKey",
                    config.host(),
                    "gov.va.med.vistalink.security.ServerPortKey",
                    config.port())
            };
          }
        };
    return new LoginContext(name, null, handler(), jaasConfiguration);
  }

  @SneakyThrows
  private VistaKernelPrincipalImpl createVistaKernelPrincipal() {
    loginContext().login();
    return VistaKernelPrincipalImpl.getKernelPrincipal(loginContext().getSubject());
  }

// -----------------------------------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------------------------------


  // TODO
  @Override
  public void close() {
    try {
      loginContext.logout();
    } catch (LoginException e) {
      log.warn("Failed to logout", e);
    }
  }

  // TODO
  @Override
  public RpcInvocationResult invoke(RpcDetails rpcDetails) {


    return null;
  }
}
