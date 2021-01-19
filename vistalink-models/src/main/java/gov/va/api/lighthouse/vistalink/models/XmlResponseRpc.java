package gov.va.api.lighthouse.vistalink.models;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

public class XmlResponseRpc implements VistaRpc {

  /** Deserialize the XML response from an RPC into expected rpc response class. */
  @SneakyThrows
  public <T> T deserialize(String xmlResponse, Class<T> clazz) {
    if (xmlResponse == null) {
      throw new VistalinkModelExceptions.InvalidVistaResponse();
    }
    try {
      return new XmlMapper()
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .readValue(xmlResponse, clazz);
    } catch (Exception e) {
      throw new VistalinkModelExceptions.InvalidVistaResponse(e);
    }
  }
}
