package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.med.vistalink.rpc.RpcResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;

public class VistalinkRpcResponseParserTest {

  @Mock RpcResponse rpcResponse;
  @Mock Document document;

  @Test
  @SneakyThrows
  void parseCanUnmarshalRpcResponse() {
    MockitoAnnotations.initMocks(this);
    Mockito.when(rpcResponse.getResultsType()).thenReturn("type");
    Mockito.when(rpcResponse.getResults()).thenReturn("results");
    Mockito.when(rpcResponse.getRawResponse())
        .thenReturn(
            "<?xml version=\"1.0\" encoding=\"utf-8\" ?><VistaLink messageType=\"gov.va.med.foundations.rpc.response\" version=\"1.6\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"rpcResponse.xsd\"><Response type=\"string\" ><![CDATA[Ping Successful!]]></Response></VistaLink>");
    Mockito.when(rpcResponse.getResultsDocument()).thenReturn(document);

    assertThat(VistalinkRpcResponseParser.parse(rpcResponse))
        .isInstanceOf(VistalinkXmlResponse.class);
  }
}
