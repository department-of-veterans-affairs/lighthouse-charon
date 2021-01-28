package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import gov.va.api.lighthouse.vistalink.models.CodeAndNameXmlField;
import gov.va.api.lighthouse.vistalink.models.ValueOnlyXmlField;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JacksonXmlRootElement(localName = "vital")
public class Vital {
  @JacksonXmlProperty ValueOnlyXmlField entered;
  @JacksonXmlProperty CodeAndNameXmlField facility;
  @JacksonXmlProperty CodeAndNameXmlField location;
  @JacksonXmlProperty List<Measurement> measurements;

  @JacksonXmlProperty
  @JacksonXmlElementWrapper(useWrapping = false)
  List<ValueOnlyXmlField> removed;

  @JacksonXmlProperty ValueOnlyXmlField taken;

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Measurement {
    @JacksonXmlProperty(isAttribute = true)
    String id;

    @JacksonXmlProperty(isAttribute = true)
    String vuid;

    @JacksonXmlProperty(isAttribute = true)
    String name;

    @JacksonXmlProperty(isAttribute = true)
    String value;

    @JacksonXmlProperty(isAttribute = true)
    String units;

    @JacksonXmlProperty(isAttribute = true)
    String metricValue;

    @JacksonXmlProperty(isAttribute = true)
    String metricUnits;

    @JacksonXmlProperty(isAttribute = true)
    String high;

    @JacksonXmlProperty(isAttribute = true)
    String low;

    @JacksonXmlProperty(isAttribute = true)
    String bmi;

    @JacksonXmlProperty List<Qualifier> qualifiers;
  }

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Qualifier {
    @JacksonXmlProperty(isAttribute = true)
    String name;

    @JacksonXmlProperty(isAttribute = true)
    String vuid;
  }
}
