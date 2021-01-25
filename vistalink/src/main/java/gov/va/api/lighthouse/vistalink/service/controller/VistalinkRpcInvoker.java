package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.api.lighthouse.vistalink.service.controller.UnrecoverableVistalinkExceptions.BadRpcContext;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.rpc.NoRpcContextFaultException;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import gov.va.med.vistalink.rpc.RpcResponse;
import gov.va.med.vistalink.security.CallbackHandlerUnitTest;
import gov.va.med.vistalink.security.VistaKernelPrincipalImpl;
import java.io.StringReader;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(onlyExplicitlyIncluded = true)
public class VistalinkRpcInvoker implements RpcInvoker, MacroExecutionContext {

  private static final JAXBContext JAXB_CONTEXT = createJaxbContext();

  private static final AtomicInteger LOGIN_CONTEXT_ID = new AtomicInteger(0);

  private final RpcPrincipal rpcPrincipal;

  private final ConnectionDetails connectionDetails;

  private final CallbackHandler handler;

  private final LoginContext loginContext;

  private final VistaKernelPrincipalImpl kernelPrincipal;

  private final VistaLinkConnection connection;

  private final MacroProcessorFactory macroProcessorFactory;

  @Builder
  VistalinkRpcInvoker(
      RpcPrincipal rpcPrincipal,
      ConnectionDetails connectionDetails,
      MacroProcessorFactory macroProcessorFactory) {
    this.rpcPrincipal = rpcPrincipal;
    this.connectionDetails = connectionDetails;
    this.macroProcessorFactory = macroProcessorFactory;
    handler = createLoginCallbackHandler();
    loginContext = createLoginContext();
    kernelPrincipal = createVistaKernelPrincipal();
    connection = createConnection();
  }

  @SneakyThrows
  private static JAXBContext createJaxbContext() {
    return JAXBContext.newInstance(VistalinkXmlResponse.class);
  }

  /** Create a response object by parsing the raw data. */
  @SneakyThrows
  public static VistalinkXmlResponse parse(RpcResponse rpcResponse) {
    Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
    return (VistalinkXmlResponse)
        unmarshaller.unmarshal(new StringReader(rpcResponse.getRawResponse()));
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

  /** Invoke an RPC with raw types. */
  @SneakyThrows
  public RpcResponse invoke(RpcRequest request) {
    synchronized (VistalinkRpcInvoker.class) {
      log.info("{} Executing RPC {}", this, request.getRpcName());
      return connection.executeRPC(request);
    }
  }

  @Override
  @SneakyThrows
  public RpcInvocationResult invoke(RpcDetails rpcDetails) {
    var start = Instant.now();
    try {
      var vistalinkRequest = RpcRequestFactory.getRpcRequest();
      vistalinkRequest.setRpcContext(rpcDetails.context());
      vistalinkRequest.setUseProprietaryMessageFormat(true);
      vistalinkRequest.setRpcName(rpcDetails.name());
      if (rpcDetails.version().isPresent()) {
        vistalinkRequest.setRpcVersion(rpcDetails.version().get());
      }
      MacroProcessor macroProcessor = macroProcessorFactory.create(this);
      for (int i = 0; i < rpcDetails.parameters().size(); i++) {
        var parameter = rpcDetails.parameters().get(i);
        var value = macroProcessor.evaluate(parameter.value().toString());
        vistalinkRequest.getParams().setParam(i + 1, parameter.type(), value);
      }
      RpcResponse vistalinkResponse = invoke(vistalinkRequest);
      log.info("{} Response {} chars", this, vistalinkResponse.getRawResponse().length());
      VistalinkXmlResponse xmlResponse = parse(vistalinkResponse);
      return RpcInvocationResult.builder()
          .vista(vista())
          .response(xmlResponse.getResponse().getValue())
          .build();
    } catch (NoRpcContextFaultException e) {
      throw new BadRpcContext(rpcDetails.context(), e);
    } finally {
      log.info(
          "{} {} ms for {}",
          this,
          Duration.between(start, Instant.now()).toMillis(),
          rpcDetails.name());
    }
  }

  @ToString.Include
  @Override
  public String vista() {
    return connectionDetails.name();
  }
}
