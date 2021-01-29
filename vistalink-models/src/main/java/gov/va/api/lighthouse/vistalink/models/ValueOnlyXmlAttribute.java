package gov.va.api.lighthouse.vistalink.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines an XML element with attribute value.
 *
 * <pre>{@code
 * <MyXmlElement value="value"/>
 *
 * }</pre>
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValueOnlyXmlAttribute {
  @JacksonXmlProperty(isAttribute = true)
  String value;
}
