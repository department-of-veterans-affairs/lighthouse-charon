package gov.va.api.lighthouse.vistalink.service.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class VistalinkPropertiesConfigTest {

  @ParameterizedTest
  @ValueSource(strings = {"host:1234", "host:nope:div", "host:1234:div:rando"})
  @NullAndEmptySource
  void asConnectionDetailsThrowsExceptionWhenValueCannotBeParsed(String value) {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> new VistalinkPropertiesConfig().asConnectionDetails("n", value));
  }

  @Test
  void loadParsesVistalinkPropertiesFromFile() {
    VistalinkProperties vp =
        new VistalinkPropertiesConfig().load("src/test/resources/vistalink.properties");
    assertThat(vp.getVistas())
        .containsExactlyInAnyOrder(
            ConnectionDetails.builder()
                .name("test")
                .host("testhost")
                .port(1111)
                .divisionIen("testdivisionien")
                .build(),
            ConnectionDetails.builder()
                .name("dummy")
                .host("dummyhost")
                .port(2222)
                .divisionIen("dummydivisionien")
                .build(),
            ConnectionDetails.builder()
                .name("fake")
                .host("fakehost")
                .port(3333)
                .divisionIen("fakedivisionien")
                .build());
  }
}
