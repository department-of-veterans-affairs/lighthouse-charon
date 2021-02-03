package gov.va.api.lighthouse.vistalink.models;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Models {

  /** Determine if an array of values are all null. */
  public static boolean allNull(Object... values) {
    for (Object value : values) {
      if (value != null) {
        return false;
      }
    }
    return true;
  }
}
