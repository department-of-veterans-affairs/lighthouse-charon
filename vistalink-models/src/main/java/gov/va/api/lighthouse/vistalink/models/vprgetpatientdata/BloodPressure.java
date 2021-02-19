package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BloodPressure {
  BloodPressureMeasurement systolic;
  BloodPressureMeasurement diastolic;

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class BloodPressureMeasurement {
    String value;
    String high;
    String low;
    String units;
  }
}
