package gov.va.api.lighthouse.charon.models.vprgetpatientdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import gov.va.api.lighthouse.charon.models.CodeAndNameXmlAttribute;
import gov.va.api.lighthouse.charon.models.ValueOnlyXmlAttribute;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Labs {
  @JacksonXmlProperty(isAttribute = true)
  Integer total;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "lab")
  List<Lab> labResults;

  /** Lazy Initializer. */
  public List<Lab> labResults() {
    if (labResults == null) {
      labResults = new ArrayList<>();
    }
    return labResults;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Lab {
    private static final Lab EMPTY = new Lab();

    @JacksonXmlProperty ValueOnlyXmlAttribute collected;

    @JacksonXmlProperty ValueOnlyXmlAttribute comment;

    @JacksonXmlProperty CodeAndNameXmlAttribute facility;

    @JacksonXmlProperty ValueOnlyXmlAttribute groupName;

    @JacksonXmlProperty ValueOnlyXmlAttribute high;

    @JacksonXmlProperty ValueOnlyXmlAttribute id;

    @JacksonXmlProperty ValueOnlyXmlAttribute interpretation;

    @JacksonXmlProperty(localName = "labOrderID")
    ValueOnlyXmlAttribute labOrderId;

    @JacksonXmlProperty ValueOnlyXmlAttribute localName;

    @JacksonXmlProperty ValueOnlyXmlAttribute loinc;

    @JacksonXmlProperty ValueOnlyXmlAttribute low;

    @JacksonXmlProperty ValueOnlyXmlAttribute performingLab;

    @JacksonXmlProperty Provider provider;

    @JacksonXmlProperty(localName = "orderID")
    ValueOnlyXmlAttribute orderId;

    @JacksonXmlProperty ValueOnlyXmlAttribute result;

    @JacksonXmlProperty ValueOnlyXmlAttribute resulted;

    @JacksonXmlProperty ValueOnlyXmlAttribute sample;

    @JacksonXmlProperty CodeAndNameXmlAttribute specimen;

    @JacksonXmlProperty ValueOnlyXmlAttribute status;

    @JacksonXmlProperty ValueOnlyXmlAttribute test;

    @JacksonXmlProperty ValueOnlyXmlAttribute type;

    @JacksonXmlProperty ValueOnlyXmlAttribute units;

    @JacksonXmlProperty ValueOnlyXmlAttribute vuid;

    /** Check if a Lab result is empty (e.g. all fields are null). */
    @JsonIgnore
    public boolean isNotEmpty() {
      return !equals(EMPTY);
    }
  }

  @Data
  @Builder
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Provider {
    @JacksonXmlProperty(isAttribute = true)
    String code;

    @JacksonXmlProperty(isAttribute = true)
    String name;

    @JacksonXmlProperty(isAttribute = true)
    String officePhone;

    @JacksonXmlProperty(isAttribute = true)
    String analogPager;

    @JacksonXmlProperty(isAttribute = true)
    String fax;

    @JacksonXmlProperty(isAttribute = true)
    String email;

    @JacksonXmlProperty(isAttribute = true)
    String taxonomyCode;

    @JacksonXmlProperty(isAttribute = true)
    String providerType;

    @JacksonXmlProperty(isAttribute = true)
    String classification;

    @JacksonXmlProperty(isAttribute = true)
    String specialization;

    @JacksonXmlProperty(isAttribute = true)
    String service;
  }
}
