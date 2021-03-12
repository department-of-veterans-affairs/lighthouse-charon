package gov.va.api.lighthouse.charon.service.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class VistalinkPropertiesConfigTest {

  @ParameterizedTest
  @ValueSource(
      strings = {
        "host:1234",
        "host:nope:div",
        "host:1234:div:timezone:rando",
        "host:1234:div:Space/Moon"
      })
  @NullAndEmptySource
  void asConnectionDetailsThrowsExceptionWhenValueCannotBeParsed(String value) {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> new VistalinkPropertiesConfig().asConnectionDetails("n", value));
  }

  @Test
  void loadParsesVistalinkPropertiesFromFile() {
    VistalinkProperties vp =
        new VistalinkPropertiesConfig().load("src/test/resources/vistalink.properties");
    assertThat(vp.vistas())
        .containsExactlyInAnyOrder(
            ConnectionDetails.builder()
                .name("test")
                .host("testhost")
                .port(1111)
                .divisionIen("testdivisionien")
                .timezone("America/Phoenix")
                .build(),
            ConnectionDetails.builder()
                .name("dummy")
                .host("dummyhost")
                .port(2222)
                .divisionIen("dummydivisionien")
                .timezone("America/Chicago")
                .build(),
            ConnectionDetails.builder()
                .name("fake")
                .host("fakehost")
                .port(3333)
                .divisionIen("fakedivisionien")
                .timezone("America/St_Johns")
                .build());
  }
}
