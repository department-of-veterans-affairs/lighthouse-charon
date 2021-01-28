package gov.va.api.lighthouse.vistalink.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeAndNameXmlField {
  @JacksonXmlProperty(isAttribute = true)
  String code;

  @JacksonXmlProperty(isAttribute = true)
  String name;
}
