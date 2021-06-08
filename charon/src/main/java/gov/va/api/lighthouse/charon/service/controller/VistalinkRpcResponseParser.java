package gov.va.api.lighthouse.charon.service.controller;

import gov.va.med.vistalink.rpc.RpcResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import lombok.SneakyThrows;

/** Parse Vistalink xml responses. */
public class VistalinkRpcResponseParser {

  private static JAXBContext JAXB_CONTEXT = createJaxbContext();

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
}
