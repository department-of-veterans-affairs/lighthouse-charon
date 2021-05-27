package gov.va.api.lighthouse.charon.models.vprgetpatientdata;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Models for blood pressure. */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BloodPressure {
  BloodPressureMeasurement systolic;
  BloodPressureMeasurement diastolic;

  /** Model for blood pressure measurements. */
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
