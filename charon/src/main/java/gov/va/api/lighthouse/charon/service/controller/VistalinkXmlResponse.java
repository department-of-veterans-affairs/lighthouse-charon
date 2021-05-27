package gov.va.api.lighthouse.charon.service.controller;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import lombok.Data;
import lombok.experimental.Accessors;

/** Model class for vistalink responses. */
@XmlRootElement(name = "VistaLink")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(fluent = false)
public class VistalinkXmlResponse {

  @XmlElement(name = "Response")
  Payload response;

  /** Payload. */
  @XmlType
  @XmlAccessorType(XmlAccessType.FIELD)
  @Data
  @Accessors(fluent = false)
  public static class Payload {
    @XmlAttribute private String type;

    @XmlValue private String value;
  }
}
