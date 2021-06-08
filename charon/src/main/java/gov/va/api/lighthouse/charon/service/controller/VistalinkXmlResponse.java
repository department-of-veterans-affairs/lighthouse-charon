package gov.va.api.lighthouse.charon.service.controller;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;
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
