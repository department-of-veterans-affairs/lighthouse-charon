package gov.va.api.lighthouse.charon.service;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.controller.DfnMacro;
import gov.va.api.lighthouse.charon.service.controller.MacroExecutionContext;
import gov.va.api.lighthouse.charon.service.controller.MacroProcessor;
import gov.va.api.lighthouse.charon.service.controller.MacroProcessorFactory;
import gov.va.api.lighthouse.charon.service.controller.VistalinkRpcInvoker;
import gov.va.api.lighthouse.charon.service.controller.VistalinkRpcInvokerFactory;
import gov.va.med.environment.Environment;
import gov.va.med.exception.FoundationsException;
import gov.va.med.vistalink.adapter.cci.VistaLinkAppProxyConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpecImpl;
import gov.va.med.vistalink.adapter.cci.VistaLinkResourceException;
import gov.va.med.vistalink.adapter.spi.EMAdapterEnvironment;
import gov.va.med.vistalink.adapter.spi.EMReAuthState;
import gov.va.med.vistalink.adapter.spi.VistaLinkConnectionImpl;
import gov.va.med.vistalink.adapter.spi.VistaLinkConnectionRequestInfo;
import gov.va.med.vistalink.adapter.spi.VistaLinkJ2SEConnSpec;
import gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnection;
import gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnectionFactory;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import gov.va.med.vistalink.rpc.RpcResponse;
import gov.va.med.vistalink.security.m.KernelSecurityHandshake;
import gov.va.med.vistalink.security.m.KernelSecurityHandshakeManaged;
import gov.va.med.vistalink.security.m.SecurityResponse;
import gov.va.med.vistalink.security.m.SecurityResponseFactory;
import gov.va.med.vistalink.security.m.SecurityVOLogon;
import java.io.Serial;
import java.util.List;
import javax.resource.spi.ConnectionRequestInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@Slf4j
public class InteractiveTest {

  @Test
  @EnabledIfSystemProperty(named = "interactive", matches = "true")
  @SneakyThrows
  void appProxyAccessVerifyCodeWithDirectComponentUsage() {

    RpcPrincipal principal =
        RpcPrincipal.builder()
            .accessCode(requirePropertyValue("app-proxy.access-code"))
            .verifyCode(requirePropertyValue("app-proxy.verify-code"))
            .build();
    String appProxyName = "LHS,APPLICATION PROXY";

    ConnectionDetails connectionDetails = connectionDetails();
    RpcDetails rpc =
        RpcDetails.builder()
            .context("LHS RPC CONTEXT")
            .name("VPR GET PATIENT DATA")
            .parameters(
                List.of(
                    RpcDetails.Parameter.builder().string("140").build(),
                    RpcDetails.Parameter.builder().string("vitals").build(),
                    RpcDetails.Parameter.builder().string("").build(),
                    RpcDetails.Parameter.builder().string("").build(),
                    RpcDetails.Parameter.builder().string("").build(),
                    RpcDetails.Parameter.builder().string("").build(),
                    RpcDetails.Parameter.builder().array(List.of()).build()))
            .build();

    MacroProcessorFactory mpf = new MacroProcessorFactory(List.of(new DfnMacro()));
    WtfVistaLinkManagedConnectionFactory mcf = new WtfVistaLinkManagedConnectionFactory();
    mcf.setPrimaryStation("673");
    mcf.setNonManagedHostPort(connectionDetails.port());
    mcf.setNonManagedHostIPAddress(connectionDetails.host());
    mcf.setNonManagedAccessCode(principal.accessCode());
    mcf.setNonManagedVerifyCode(principal.verifyCode());
    mcf.setAdapterEnvironment(EMAdapterEnvironment.J2EE); // DEFAULT J2SE

    VistaLinkManagedConnection mc = new WtfVistaLinkManagedConnection(mcf, 666);

    VistaLinkConnectionSpecImpl cs =
        new VistaLinkAppProxyConnectionSpec(connectionDetails.divisionIen(), appProxyName);
    ConnectionRequestInfo connReq = new VistaLinkConnectionRequestInfo(cs);
    VistaLinkConnectionImpl connection = (VistaLinkConnectionImpl) mc.getConnection(null, connReq);
    RpcRequest request = toRpcRequest(mpf, rpc);
    request.setReAuthenticateInfo(cs, EMReAuthState.VIRGIN, EMAdapterEnvironment.J2EE);
    var response = connection.executeRPC(request);

    log.info("{}", response.getRawResponse());
  }

  private ConnectionDetails connectionDetails() {
    return ConnectionDetails.builder()
        .name("673")
        .host("localhost")
        .port(18673)
        .divisionIen("673")
        .timezone("America/New_York")
        .build();
  }

  private String requirePropertyValue(String name) {
    String value = System.getProperty(name);
    assertThat(value).withFailMessage("System property %s not defined", name).isNotBlank();
    return value;
  }

  @Test
  @EnabledIfSystemProperty(named = "interactive", matches = "true")
  @SneakyThrows
  void standardAccessVerifyCodeUsingCharonApi() {

    RpcPrincipal principal =
        RpcPrincipal.builder()
            .accessCode(requirePropertyValue("standard.access-code"))
            .verifyCode(requirePropertyValue("standard.verify-code"))
            .build();
    ConnectionDetails connectionDetails = connectionDetails();
    RpcDetails rpc =
        RpcDetails.builder().context("XOBV VISTALINK TESTER").name("XOBV TEST PING").build();

    MacroProcessorFactory mpf = new MacroProcessorFactory(List.of(new DfnMacro()));
    VistalinkRpcInvokerFactory rif = new VistalinkRpcInvokerFactory(mpf);
    VistalinkRpcInvoker invoker = (VistalinkRpcInvoker) rif.create(principal, connectionDetails);
    RpcInvocationResult result = invoker.invoke(rpc);
    log.info("{}", result);
  }

  @Test
  @EnabledIfSystemProperty(named = "interactive", matches = "true")
  @SneakyThrows
  void standardAccessVerifyCodeWithDirectComponentUsage() {

    RpcPrincipal principal =
        RpcPrincipal.builder()
            .accessCode(requirePropertyValue("standard.access-code"))
            .verifyCode(requirePropertyValue("standard.verify-code"))
            .build();
    ConnectionDetails connectionDetails = connectionDetails();
    RpcDetails rpc =
        RpcDetails.builder().context("XOBV VISTALINK TESTER").name("XOBV TEST PING").build();

    WtfVistaLinkManagedConnectionFactory mcf = new WtfVistaLinkManagedConnectionFactory();
    mcf.setNonManagedHostPort(connectionDetails.port());
    mcf.setNonManagedHostIPAddress(connectionDetails.host());
    mcf.setAdapterEnvironment(EMAdapterEnvironment.J2SE); // DEFAULT J2SE
    VistaLinkManagedConnection mc = new WtfVistaLinkManagedConnection(mcf, 666);

    SecurityResponse securityResponse =
        KernelSecurityHandshakeManaged.doSetupAndGetIntroText(
            mc, new SecurityResponseFactory(), false, Environment.isProduction(), "");
    log.info("Security Response {}", securityResponse.getRawResponse());
    VistaLinkConnectionSpecImpl cs = new VistaLinkJ2SEConnSpec(connectionDetails.divisionIen());
    ConnectionRequestInfo connReq = new VistaLinkConnectionRequestInfo(cs);
    VistaLinkConnectionImpl connection = (VistaLinkConnectionImpl) mc.getConnection(null, connReq);

    SecurityVOLogon logon =
        KernelSecurityHandshake.doAVLogon(
                connection,
                new SecurityResponseFactory(),
                principal.accessCode(),
                principal.verifyCode(),
                false)
            .getSecurityVOLogon();

    MacroProcessorFactory mpf = new MacroProcessorFactory(List.of(new DfnMacro()));
    RpcRequest request = toRpcRequest(mpf, rpc);
    request.setReAuthenticateInfo(cs, EMReAuthState.AUTHENTICATED, EMAdapterEnvironment.J2SE);
    var response = connection.executeRPC(request);
    log.info("{}", response.getRawResponse());
  }

  RpcRequest toRpcRequest(MacroProcessorFactory macroProcessorFactory, RpcDetails rpcDetails)
      throws FoundationsException {
    var vistalinkRequest = RpcRequestFactory.getRpcRequest();
    vistalinkRequest.setRpcContext(rpcDetails.context());
    vistalinkRequest.setUseProprietaryMessageFormat(true);
    vistalinkRequest.setRpcName(rpcDetails.name());
    if (rpcDetails.version().isPresent()) {
      vistalinkRequest.setRpcVersion(rpcDetails.version().get());
    }
    MacroProcessor macroProcessor =
        macroProcessorFactory.create(
            new MacroExecutionContext() {
              @Override
              public RpcResponse invoke(RpcRequest request) {
                return null;
              }
            });
    for (int i = 0; i < rpcDetails.parameters().size(); i++) {
      var parameter = rpcDetails.parameters().get(i);
      var value = macroProcessor.evaluate(parameter);
      vistalinkRequest.getParams().setParam(i + 1, parameter.type(), value);
    }
    return vistalinkRequest;
  }

  public static class WtfVistaLinkManagedConnection extends VistaLinkManagedConnection {

    protected WtfVistaLinkManagedConnection(
        VistaLinkManagedConnectionFactory mcf, long distinguishedIdentifier)
        throws VistaLinkResourceException {
      super(mcf, distinguishedIdentifier);
    }
  }

  public static class WtfVistaLinkManagedConnectionFactory
      extends VistaLinkManagedConnectionFactory {

    @Serial private static final long serialVersionUID = 7622503603723838369L;

    @Override
    protected void setPrimaryStation(String primaryStation) {
      super.setPrimaryStation(primaryStation);
    }
  }
}
