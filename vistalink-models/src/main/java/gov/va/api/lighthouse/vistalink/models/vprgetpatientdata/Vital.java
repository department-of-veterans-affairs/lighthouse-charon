package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class Vital {
  BigDecimal entered;
  Facility facility;
  Location location;
  List<Measurement> measurements;
  BigDecimal taken;

  @Data
  static class Facility {
    String code;
    String name;
  }

  @Data
  static class Location {
    String code;
    String name;
  }

  @Data
  static class Measurement {
    int id;
    int vuid;
    String name;
    String value;
    String units;
    String metricValue;
    String metricUnits;
  }
}
