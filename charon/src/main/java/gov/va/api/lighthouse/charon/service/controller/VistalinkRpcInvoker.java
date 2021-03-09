package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import gov.va.api.lighthouse.charon.api.RpcMetadata;
import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.med.vistalink.rpc.NoRpcContextFaultException;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import gov.va.med.vistalink.rpc.RpcResponse;
import java.io.StringReader;
import java.time.Duration;
import java.time.Instant;
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

  private final ConnectionDetails connectionDetails;

  private final VistalinkSession session;

  private final MacroProcessorFactory macroProcessorFactory;

  @Builder
  VistalinkRpcInvoker(
      RpcPrincipal rpcPrincipal,
      ConnectionDetails connectionDetails,
      MacroProcessorFactory macroProcessorFactory) {
    this.connectionDetails = connectionDetails;
    this.macroProcessorFactory = macroProcessorFactory;
    this.session = chooseSession(rpcPrincipal);
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

  private VistalinkSession chooseSession(RpcPrincipal rpcPrincipal) {
    //noinspection EnhancedSwitchMigration
    switch (rpcPrincipal.type()) {
      case STANDARD_USER:
        return StandardUserVistalinkSession.builder()
            .connectionDetails(connectionDetails)
            .accessCode(rpcPrincipal.accessCode())
            .verifyCode(rpcPrincipal.verifyCode())
            .build();
      case APPLICATION_PROXY_USER:
        return ApplicationProxyUserVistalinkSession.builder()
            .connectionDetails(connectionDetails)
            .accessCode(rpcPrincipal.accessCode())
            .verifyCode(rpcPrincipal.verifyCode())
            .applicationProxyUser(rpcPrincipal.applicationProxyUser())
            .build();
      default:
        throw new IllegalArgumentException(
            "Unsupported RPC principal type: " + rpcPrincipal.type());
    }
  }

  @Override
  public void close() {
    session.close();
  }

  /** Invoke an RPC with raw types. */
  @Override
  @SneakyThrows
  public RpcResponse invoke(RpcRequest request) {
    synchronized (VistalinkRpcInvoker.class) {
      log.info("{} Executing RPC {}", this, request.getRpcName());
      return session.connection().executeRPC(request);
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
        var value = macroProcessor.evaluate(parameter);
        vistalinkRequest.getParams().setParam(i + 1, parameter.type(), value);
      }
      RpcResponse vistalinkResponse = invoke(vistalinkRequest);
      log.info("{} Response {} chars", this, vistalinkResponse.getRawResponse().length());
      VistalinkXmlResponse xmlResponse = parse(vistalinkResponse);
      return RpcInvocationResult.builder()
          .vista(vista())
          .metadata(RpcMetadata.builder().timezone(connectionDetails.timezone()).build())
          .response(xmlResponse.getResponse().getValue())
          .build();
    } catch (NoRpcContextFaultException e) {
      throw new UnrecoverableVistalinkExceptions.BadRpcContext(rpcDetails.context(), e);
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
