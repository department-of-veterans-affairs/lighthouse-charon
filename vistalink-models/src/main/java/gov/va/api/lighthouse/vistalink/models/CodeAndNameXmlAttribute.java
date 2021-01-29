package gov.va.api.lighthouse.vistalink.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines an XML element with attributes code and name.
 *
 * <pre>{@code
 * <MyXmlElement code="code" name="name"/>
 *
 * }</pre>
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeAndNameXmlAttribute {
  @JacksonXmlProperty(isAttribute = true)
  String code;

  @JacksonXmlProperty(isAttribute = true)
  String name;
}
