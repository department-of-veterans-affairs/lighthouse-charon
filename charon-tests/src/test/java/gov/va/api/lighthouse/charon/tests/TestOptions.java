package gov.va.api.lighthouse.charon.tests;

import static org.assertj.core.api.Assumptions.assumeThat;

import java.util.Locale;
import org.apache.commons.lang3.BooleanUtils;

public class TestOptions {
  private static String asEnvVariable(String systemProperty) {
    return systemProperty.toUpperCase(Locale.ENGLISH).replace('.', '_');
  }

  public static void assumeEnabled(String systemProperty) {
    assumeThat(isEnabled(systemProperty, false))
        .as(
            "Set system property '%s' or environment variable '%s' to 'true' to enable.",
            systemProperty, asEnvVariable(systemProperty))
        .isTrue();
  }

  public static boolean isEnabled(String systemProperty, boolean defaultValue) {
    return BooleanUtils.toBoolean(valueOf(systemProperty, Boolean.toString(defaultValue)));
  }

  public static String valueOf(String systemProperty, String defaultValue) {
    String value = System.getProperty(systemProperty);
    if (value == null || value.isBlank()) {
      value = System.getenv(asEnvVariable(systemProperty));
    }
    return value == null ? defaultValue : value;
  }
}
