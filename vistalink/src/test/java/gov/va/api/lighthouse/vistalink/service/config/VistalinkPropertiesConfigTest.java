package gov.va.api.lighthouse.vistalink.service.config;

import org.junit.jupiter.api.Test;

public class VistalinkPropertiesConfigTest {

  @Test
  void loadParsesVistalinkPropertiesFromFile() {
    VistalinkProperties vp =
        VistalinkPropertiesConfig.builder().build().load("vistalink.properties");
  }
}
