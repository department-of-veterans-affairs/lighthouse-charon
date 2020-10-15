package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.service.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.service.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import gov.va.med.vistalink.rpc.RpcResponse;
import gov.va.med.vistalink.security.CallbackHandlerUnitTest;
import gov.va.med.vistalink.security.VistaKernelPrincipalImpl;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import lombok.Builder;
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

  private final CallbackHandler handler = createLoginCallbackHandler();

  private final LoginContext loginContext = createLoginContext();

  private final VistaKernelPrincipalImpl principal = createVistaKernelPrincipal();

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
        rpcPrincipal.getAccessCode(),
        rpcPrincipal.getVerifyCode(),
        connectionDetails.getDivisionIen());
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
                      connectionDetails.getHost(),
                      "gov.va.med.vistalink.security.ServerPortKey",
                      connectionDetails.getPort()))
            };
          }
        };
    return new LoginContext("vlx:" + connectionDetails.getHost(), null, handler, jaasConfiguration);
  }

  @SneakyThrows
  private VistaKernelPrincipalImpl createVistaKernelPrincipal() {
    loginContext.login();
    return VistaKernelPrincipalImpl.getKernelPrincipal(loginContext.getSubject());
  }

  @Override
  public void close() {
    try {
      loginContext.logout();
    } catch (LoginException e) {
      log.warn("Failed to logout", e);
    }
  }

  /** Invoke an RPC with raw types. */
  @SneakyThrows
  public RpcResponse invoke(RpcRequest request) {
    return connection.executeRPC(request);
  }

  // TODO
  @Override
  @SneakyThrows
  public RpcInvocationResult invoke(RpcDetails rpcDetails) {
    var start = Instant.now();
    try {

      var request = RpcRequestFactory.getRpcRequest();
      request.setRpcContext(rpcDetails.getContext());
      request.setUseProprietaryMessageFormat(true);

      request.setRpcName(rpcDetails.getName());

      // TODO: Do we need version?
      /*
        if (version != null) {
        request.setRpcVersion(version);
      }
      */

      for (int i = 0; i < rpcDetails.getParameters().size(); i++) {
        var parameter = rpcDetails.getParameters().get(i);
        request.getParams().setParam(i + 1, parameter.type(), parameter.value());
      }

      RpcResponse response = invoke(request);

      // TODO: Do something with this response? getRawResponse(), getResponse()? Parse xml?
      response.getRawResponse();
      // TODO: Turn XML into RpcInvocationResults
      return RpcInvocationResult.builder().build();

    } finally {
      log.info(
          "{} ms for {}", Duration.between(start, Instant.now()).toMillis(), rpcDetails.getName());
    }
  }
}
