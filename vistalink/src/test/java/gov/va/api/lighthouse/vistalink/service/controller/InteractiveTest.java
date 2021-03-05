package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.med.environment.Environment;
import gov.va.med.exception.FoundationsException;
import gov.va.med.vistalink.adapter.cci.VistaLinkAppProxyConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpecImpl;
import gov.va.med.vistalink.adapter.cci.VistaLinkResourceException;
import gov.va.med.vistalink.adapter.spi.EMAdapterEnvironment;
import gov.va.med.vistalink.adapter.spi.EMReAuthState;
import gov.va.med.vistalink.adapter.spi.VistaLinkConnectionImpl;
import gov.va.med.vistalink.adapter.spi.VistaLinkConnectionRequestInfo;
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
import java.util.List;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Level;
import org.junit.jupiter.api.Test;

@Slf4j
public class InteractiveTest {

  @Test
  @SneakyThrows
  void ping() {
    String appProxyName = "LHS,CONNECTOR PROXY";
    appProxyName = "LHS,APPLICATION PROXY";

    RpcPrincipal principal =
        RpcPrincipal.builder().accessCode("1PROGRAMMER").verifyCode("PROGRAMMER1").build();
    principal =
        RpcPrincipal.builder().accessCode("123LIGHTHOUSE").verifyCode("321LIGHTHOUSE*").build();

    ConnectionDetails connectionDetails =
        ConnectionDetails.builder()
            .name("673")
            .host("localhost")
            .port(18673)
            .divisionIen("673")
            .timezone("America/New_York")
            .build();
    RpcDetails rpc =
        RpcDetails.builder().context("XOBV VISTALINK TESTER").name("XOBV TEST PING").build();
    rpc =
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

    org.apache.log4j.Logger.getLogger("gov.va.med.vistalink").setLevel(Level.DEBUG);

    MacroProcessorFactory mpf = new MacroProcessorFactory(List.of(new DfnMacro()));

    boolean endorsed = !true;
    boolean hack = !endorsed;

    if (endorsed) {
      VistalinkRpcInvokerFactory rif = new VistalinkRpcInvokerFactory(mpf);
      VistalinkRpcInvoker invoker = (VistalinkRpcInvoker) rif.create(principal, connectionDetails);
      RpcInvocationResult result = invoker.invoke(rpc);
      log.info("{}", result);
      log.error("--------------------------------------------------------------------");
    }

    if (hack) {
      WtfVistaLinkManagedConnectionFactory mcf = new WtfVistaLinkManagedConnectionFactory();
      mcf.setPrimaryStation("673");
      mcf.setNonManagedHostPort(connectionDetails.port());
      mcf.setNonManagedHostIPAddress(connectionDetails.host());
      mcf.setNonManagedAccessCode(principal.accessCode());
      mcf.setNonManagedVerifyCode(principal.verifyCode());
      mcf.setAdapterEnvironment(EMAdapterEnvironment.J2EE); // DEFAULT J2SE

      VistaLinkManagedConnection mc = new WtfVistaLinkManagedConnection(mcf, 666);
      mc.addConnectionEventListener(new SpamConnectionEventListener());

      VistaLinkConnectionSpecImpl cs =
          new VistaLinkAppProxyConnectionSpec(connectionDetails.divisionIen(), appProxyName);
      ConnectionRequestInfo connReq = new VistaLinkConnectionRequestInfo(cs);
      VistaLinkConnectionImpl connection =
          (VistaLinkConnectionImpl) mc.getConnection(null, connReq);

      // VistaLinkConnectionImpl connection = new VistaLinkConnectionImpl(mc);

      if (false) {
        log.info("Starting KernelSecurityHandshakeManaged.doSetupAndGetIntroText");
        SecurityResponse securityResponse =
            KernelSecurityHandshakeManaged.doSetupAndGetIntroText(
                mc,
                new SecurityResponseFactory(),
                true,
                Environment.isProduction(),
                mcf.getPrimaryStation());
        log.info(
            "Security Response {}/{}/{}",
            securityResponse.getResultType(),
            securityResponse.getResultMessage(),
            securityResponse.getRawResponse());

        SecurityVOLogon logon =
            KernelSecurityHandshake.doAVLogon(
                    connection,
                    new SecurityResponseFactory(),
                    principal.accessCode(),
                    principal.verifyCode(),
                    false)
                .getSecurityVOLogon();

        log.info(
            "Logon {}/{}/{}",
            logon.getPostSignInText(),
            logon.getResultMessage(),
            logon.getDivisionList());
      }

      RpcRequest request = toRpcRequest(mpf, rpc);
      request.setReAuthenticateInfo(cs, EMReAuthState.VIRGIN, EMAdapterEnvironment.J2EE);
      // new VistaLinkJ2SEConnSpec(connectionDetails.divisionIen());
      //    request.setReAuthenticateInfo(cs, EMReAuthState.VIRGIN, EMAdapterEnvironment.J2SE);
      var response = connection.executeRPC(request);

      log.info("{}", response.getRawResponse());
    }
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

  private static class SpamConnectionEventListener implements ConnectionEventListener {

    @Override
    public void connectionClosed(ConnectionEvent connectionEvent) {
      spam("CLOSED", connectionEvent);
    }

    @Override
    public void connectionErrorOccurred(ConnectionEvent connectionEvent) {
      spam("CLOSED", connectionEvent);
    }

    @Override
    public void localTransactionCommitted(ConnectionEvent connectionEvent) {
      spam("CLOSED", connectionEvent);
    }

    @Override
    public void localTransactionRolledback(ConnectionEvent connectionEvent) {
      spam("CLOSED", connectionEvent);
    }

    @Override
    public void localTransactionStarted(ConnectionEvent connectionEvent) {
      spam("CLOSED", connectionEvent);
    }

    void spam(String why, ConnectionEvent e) {
      log.info(
          "{} -> {}/{}/{}/{}",
          why,
          e.getId(),
          e.getSource(),
          e.getConnectionHandle(),
          e.getException());
    }
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

    @Override
    protected void setPrimaryStation(String primaryStation) {
      super.setPrimaryStation(primaryStation);
    }
  }
}
