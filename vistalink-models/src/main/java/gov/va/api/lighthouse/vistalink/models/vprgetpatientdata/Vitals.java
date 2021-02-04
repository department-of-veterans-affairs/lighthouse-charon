package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import gov.va.api.lighthouse.vistalink.models.CodeAndNameXmlAttribute;
import gov.va.api.lighthouse.vistalink.models.ValueOnlyXmlAttribute;
import java.util.ArrayList;
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
public class Vitals {
  @JacksonXmlProperty(isAttribute = true)
  Integer total;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "vital")
  List<Vital> vitalResults;

  /** Lazy Initializer. */
  public List<Vital> vitalResults() {
    if (vitalResults == null) {
      vitalResults = new ArrayList<>();
    }
    return vitalResults;
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

    @JacksonXmlProperty List<Qualifier> qualifiers;

    BloodPressure asBloodPressure() {
      if (high != null && low != null && value != null) {
        String[] highs = high.split("/", -1);
        String[] lows = low.split("/", -1);
        String[] values = value.split("/", -1);
        if (highs.length == 2 && lows.length == 2 && values.length == 2) {
          return BloodPressure.builder()
              .systolic(
                  BloodPressure.BloodPressureMeasurement.builder()
                      .high(highs[0])
                      .low(lows[0])
                      .units(units)
                      .value(values[0])
                      .build())
              .diastolic(
                  BloodPressure.BloodPressureMeasurement.builder()
                      .high(highs[1])
                      .low(lows[1])
                      .units(units)
                      .value(values[1])
                      .build())
              .build();
        }
      }
      return null;
    }

    boolean isBloodPressure() {
      return "BLOOD PRESSURE".equals(name);
    }
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

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @JacksonXmlRootElement(localName = "vital")
  public static class Vital {
    private static final Vital EMPTY = new Vital();

    @JacksonXmlProperty ValueOnlyXmlAttribute entered;

    @JacksonXmlProperty CodeAndNameXmlAttribute facility;

    @JacksonXmlProperty CodeAndNameXmlAttribute location;

    @JacksonXmlProperty List<Measurement> measurements;

    @JacksonXmlProperty
    @JacksonXmlElementWrapper(useWrapping = false)
    List<ValueOnlyXmlAttribute> removed;

    @JacksonXmlProperty ValueOnlyXmlAttribute taken;

    /** Check if a Vital result is empty (e.g. all fields are null). */
    public boolean isNotEmpty() {
      return !equals(EMPTY);
    }
  }
}
