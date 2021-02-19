package gov.va.api.lighthouse.vistalink.models;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Data
@Builder
@AllArgsConstructor(staticName = "of")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValueOnlyXmlAttribute {
  @JacksonXmlProperty(isAttribute = true)
  String value;
}
