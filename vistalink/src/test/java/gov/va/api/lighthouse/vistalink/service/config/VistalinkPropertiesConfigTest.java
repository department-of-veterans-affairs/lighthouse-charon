package gov.va.api.lighthouse.vistalink.service.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class VistalinkPropertiesConfigTest {

  @Test
  void loadParsesVistalinkPropertiesFromFile() {
    VistalinkProperties vp =
        VistalinkPropertiesConfig.builder().build().load("vistalink.properties");
    assertThat(vp.getVistas())
        .containsExactlyInAnyOrder(
            ConnectionDetails.builder()
                .host("testhost")
                .port(1111)
                .divisionIen("testdivisionien")
                .build(),
            ConnectionDetails.builder()
                .host("dummyhost")
                .port(2222)
                .divisionIen("dummydivisionien")
                .build(),
            ConnectionDetails.builder()
                .host("fakehost")
                .port(3333)
                .divisionIen("fakedivisionien")
                .build());
  }
}
