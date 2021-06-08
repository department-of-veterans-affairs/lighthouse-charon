package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcDetails.Parameter;
import gov.va.api.lighthouse.charon.api.RpcMetadata;
import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.controller.FugaziMacros.AppendXMacro;
import gov.va.api.lighthouse.charon.service.controller.FugaziMacros.ToUpperCaseMacro;
import gov.va.api.lighthouse.charon.service.controller.UnrecoverableVistalinkExceptions.BadRpcContext;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.adapter.record.VistaLinkFaultException;
import gov.va.med.vistalink.rpc.NoRpcContextFaultException;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcResponse;
import jakarta.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Document;

@ExtendWith(MockitoExtension.class)
public class VistalinkRpcInvokerFactoryTest {

  @Mock private VistalinkSession session;
  @Mock private VistaLinkConnection connection;

  ConnectionDetails connectionDetails() {
    return ConnectionDetails.builder()
        .name("fugazi")
        .host("fugazi.com")
        .port(666)
        .divisionIen("666")
        .timezone("America/New_York")
        .build();
  }

  VistalinkRpcInvoker createInvoker() {
    var macroProcessor =
        new MacroProcessorFactory(
            List.of(new FugaziMacros.AppendXMacro(), new FugaziMacros.ToUpperCaseMacro()));

    return VistalinkRpcInvoker.builder()
        .macroProcessorFactory(macroProcessor)
        .rpcPrincipal(RpcPrincipal.standardUserBuilder().accessCode("a").verifyCode("v").build())
        .connectionDetails(connectionDetails())
        .optionalSessionSelection((r, c) -> session)
        .build();
  }

  @Test
  void createReturnsAnRpcInvokerFactory() {
    VistalinkRpcInvokerFactory f =
        new VistalinkRpcInvokerFactory(
            new MacroProcessorFactory(List.of(new AppendXMacro(), new ToUpperCaseMacro())));
    RpcInvoker in =
        f.create(
            RpcPrincipal.standardUserBuilder().accessCode("a").verifyCode("v").build(),
            connectionDetails());
    assertThat(in.vista()).isEqualTo(connectionDetails().name());
  }

  RpcResponse fugaziRpcResponse(String payloadValue) throws JAXBException {
    var xmlResponse = new VistalinkXmlResponse();
    var xmlPayload = new VistalinkXmlResponse.Payload();
    xmlPayload.setType("fugazi");
    xmlPayload.setValue(payloadValue);
    xmlResponse.setResponse(xmlPayload);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    VistalinkRpcInvoker.createJaxbContext().createMarshaller().marshal(xmlResponse, outputStream);
    RpcResponse response =
        new FugaziRpcResponse(
            outputStream.toString(StandardCharsets.UTF_8), null, null, null, null, null);
    return response;
  }

  @Test
  @SneakyThrows
  void invokeReturnsResultAndClosesSession() {
    var captor = ArgumentCaptor.forClass(gov.va.med.vistalink.rpc.RpcRequest.class);
    when(session.connection()).thenReturn(connection);
    RpcResponse response = fugaziRpcResponse("fake boi");
    when(connection.executeRPC(captor.capture())).thenReturn(response);

    try (VistalinkRpcInvoker invoker = createInvoker()) {
      var result =
          invoker.invoke(
              RpcDetails.builder()
                  .context("MA CONTEXT")
                  .name("MA NAME")
                  .parameters(List.of(Parameter.builder().string("hello").build()))
                  .build());

      assertThat(result.error()).isEmpty();
      assertThat(result.metadata())
          .isEqualTo(RpcMetadata.builder().timezone(connectionDetails().timezone()).build());
      assertThat(result.response()).isEqualTo("fake boi");
    }

    var request = captor.getValue();
    assertThat(request.getRpcContext()).isEqualTo("MA CONTEXT");
    assertThat(request.getRpcName()).isEqualTo("MA NAME");
    assertThat(request.getParams().getParam(1)).isEqualTo("hello");

    verify(session).connection();
    verify(session).close();
  }

  @Test
  @SneakyThrows
  void invokeThrowsBadContextExceptionWhenContextIsUnknown() {
    when(session.connection()).thenReturn(connection);
    RpcResponse response = fugaziRpcResponse("fake boi");
    when(connection.executeRPC(any(RpcRequest.class)))
        .thenThrow(new NoRpcContextFaultException(new VistaLinkFaultException()));

    assertThatExceptionOfType(BadRpcContext.class)
        .isThrownBy(
            () ->
                createInvoker()
                    .invoke(
                        RpcDetails.builder()
                            .context("MA CONTEXT")
                            .name("MA NAME")
                            .parameters(
                                List.of(
                                    Parameter.builder().string("${touppercase(hello)}").build()))
                            .build()));
  }

  @Test
  @SneakyThrows
  void invokeUsesMacros() {
    var captor = ArgumentCaptor.forClass(gov.va.med.vistalink.rpc.RpcRequest.class);
    when(session.connection()).thenReturn(connection);
    RpcResponse response = fugaziRpcResponse("fake boi");
    when(connection.executeRPC(captor.capture())).thenReturn(response);

    try (VistalinkRpcInvoker invoker = createInvoker()) {
      var result =
          invoker.invoke(
              RpcDetails.builder()
                  .context("MA CONTEXT")
                  .name("MA NAME")
                  .parameters(List.of(Parameter.builder().string("${touppercase(hello)}").build()))
                  .build());
    }

    var request = captor.getValue();
    assertThat(request.getRpcContext()).isEqualTo("MA CONTEXT");
    assertThat(request.getRpcName()).isEqualTo("MA NAME");
    assertThat(request.getParams().getParam(1)).isEqualTo("HELLO");
  }

  @Test
  @SneakyThrows
  void invokeUsesVersionIfPresent() {
    var captor = ArgumentCaptor.forClass(gov.va.med.vistalink.rpc.RpcRequest.class);
    when(session.connection()).thenReturn(connection);
    RpcResponse response = fugaziRpcResponse("fake boi");
    when(connection.executeRPC(captor.capture())).thenReturn(response);

    try (VistalinkRpcInvoker invoker = createInvoker()) {
      var result =
          invoker.invoke(
              RpcDetails.builder()
                  .context("MA CONTEXT")
                  .name("MA NAME")
                  .version(Optional.of(1.2))
                  .parameters(List.of(Parameter.builder().string("hello").build()))
                  .build());
    }

    var request = captor.getValue();
    assertThat(request.getRpcContext()).isEqualTo("MA CONTEXT");
    assertThat(request.getRpcName()).isEqualTo("MA NAME");
    assertThat(request.getRpcVersion()).isEqualTo(1.2);
    assertThat(request.getParams().getParam(1)).isEqualTo("hello");
  }

  @Test
  void sessionSelection() {
    assertThat(
            VistalinkRpcInvoker.chooseSession(
                RpcPrincipal.standardUserBuilder().accessCode("a").verifyCode("v").build(),
                connectionDetails()))
        .isInstanceOf(StandardUserVistalinkSession.class);
    assertThat(
            VistalinkRpcInvoker.chooseSession(
                RpcPrincipal.applicationProxyUserBuilder()
                    .accessCode("a")
                    .verifyCode("v")
                    .applicationProxyUser("u")
                    .build(),
                connectionDetails()))
        .isInstanceOf(ApplicationProxyUserVistalinkSession.class);
  }

  private static class FugaziRpcResponse extends RpcResponse {

    FugaziRpcResponse(
        String rawXml,
        String filteredXml,
        Document doc,
        String messageType,
        String cdataFromXml,
        String resultsType) {
      super(rawXml, filteredXml, doc, messageType, cdataFromXml, resultsType);
    }
  }
}
