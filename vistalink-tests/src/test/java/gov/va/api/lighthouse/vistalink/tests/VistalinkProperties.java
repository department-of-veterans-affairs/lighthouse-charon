package gov.va.api.lighthouse.vistalink.tests;

import lombok.Value;
import lombok.experimental.UtilityClass;

@UtilityClass
@Value
public final class VistalinkProperties {

  String vistaAccessCode = vistaAccessCode();
  String vistaVerifyCode = vistaVerifyCode();

  private String vistaAccessCode() {
    return System.getProperty("vista.access-code", "not-set");
  }

  private String vistaVerifyCode() {
    return System.getProperty("vista.verify-code", "not-set");
  }
}
