package gov.va.api.lighthouse.vistalink.tests;

import lombok.experimental.UtilityClass;

@UtilityClass
class VistalinkProperties {

  static String vistaAccessCode() {
    return System.getProperty("vista.access-code", "not-set");
  }

  static String vistaVerifyCode() {
    return System.getProperty("vista.verify-code", "not-set");
  }
}
