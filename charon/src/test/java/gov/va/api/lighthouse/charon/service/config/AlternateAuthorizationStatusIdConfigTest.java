package gov.va.api.lighthouse.charon.service.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gov.va.api.lighthouse.charon.service.controller.AlternateAuthorizationStatusIds;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AlternateAuthorizationStatusIdConfigTest {

  @ParameterizedTest
  @ValueSource(
      strings = {
        "bruce@manor:",
        "bruce@manor:@",
        ":bruce@manor",
        "@:bruce@manor",
        "bruce@manor:batman",
        "bruce@manor:batman@",
        "bruce:batman@cave",
        "@manor:batman@cave",
        "bruce@manor",
        "like no way"
      })
  void invalidMappingsThrowException(String badMapping) {
    var mappings = List.of("ad@as:AD@AS", "bd@bs:BD@BS", badMapping, "cd@cs:CD@CS");
    AlternateAuthorizationStatusIdProperties properties =
        new AlternateAuthorizationStatusIdProperties(true, mappings);
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(
            () ->
                new AlternateAuthorizationStatusIdConfig()
                    .alternateAuthorizationStatusIds(properties));
  }

  @Test
  void validMappingsAreAccepted() {
    var mappings = List.of("bruce@manor:batman@cave", "alsobruce@manor:batman@cave:its@all:cave");
    AlternateAuthorizationStatusIdProperties properties =
        new AlternateAuthorizationStatusIdProperties(true, mappings);
    AlternateAuthorizationStatusIds alts =
        new AlternateAuthorizationStatusIdConfig().alternateAuthorizationStatusIds(properties);

    assertThat(alts.toPrivateId(AuthorizationId.of("bruce@manor")))
        .isEqualTo(AuthorizationId.of("batman@cave"));
    assertThat(alts.toPrivateId(AuthorizationId.of("alsobruce@manor")))
        .isEqualTo(AuthorizationId.of("batman@cave:its@all:cave"));
  }
}
