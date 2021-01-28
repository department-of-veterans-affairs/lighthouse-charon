package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
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
  @JacksonXmlProperty Entered entered;
  @JacksonXmlProperty Facility facility;
  @JacksonXmlProperty Location location;
  @JacksonXmlProperty List<Measurement> measurements;

  @JacksonXmlProperty
  @JacksonXmlElementWrapper(useWrapping = false)
  List<Removed> removed;

  @JacksonXmlProperty Taken taken;

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Entered {
    @JacksonXmlProperty(isAttribute = true)
    String value;
  }

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Facility {
    @JacksonXmlProperty(isAttribute = true)
    String code;

    @JacksonXmlProperty(isAttribute = true)
    String name;
  }

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Location {
    @JacksonXmlProperty(isAttribute = true)
    String code;

    @JacksonXmlProperty(isAttribute = true)
    String name;
  }

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
  }

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Removed {
    @JacksonXmlProperty(isAttribute = true)
    String value;
  }

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Taken {
    @JacksonXmlProperty(isAttribute = true)
    String value;
  }
}
