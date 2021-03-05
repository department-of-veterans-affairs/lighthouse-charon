package gov.va.api.lighthouse.charon.service.controller;

import gov.va.med.vistalink.rpc.RpcResponse;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    // TODO Unsure of what the best way of how to get the results
    try {
      log.info("TYPE ...... {}", rpcResponse.getResultsType());
      log.info("RESULTS ... {}", rpcResponse.getResults());
      log.info("RAW ....... {}", rpcResponse.getRawResponse());
      log.info("DOCUMENT .. {}", rpcResponse.getResultsDocument());
    } catch (Exception e) {
      log.info("MMMM ...... {}", e.getMessage());
    }
    return (VistalinkXmlResponse)
        unmarshaller.unmarshal(new StringReader(rpcResponse.getRawResponse()));
  }
}
