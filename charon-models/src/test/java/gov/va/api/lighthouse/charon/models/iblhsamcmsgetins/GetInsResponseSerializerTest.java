package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class GetInsResponseSerializerTest {
  @Test
  void deserialize() {
    String results =
        String.join(
            "\n",
            "2.312^1^1.03^2020/01/20^3200120",
            "2.322^2^.03^50^50",
            "36^3^.01^Shanktopus!^SHANKTOPUS",
            "355.3^4^.01^Big Boi!^BIG BOY",
            "355.32^5^2^Oh my!^EHRMAHGERD!",
            "999^6^999^EW^DAVID");
    var sample = GetInsResponseSerializer.create().deserialize(results);
    var expected =
        GetInsRpcResults.builder()
            .insTypeDateLastVerified(entry("2.312", "1", "1.03", "2020/01/20", "3200120"))
            .elBenefitCoverageLevel(entry("2.322", "2", ".03", "50", "50"))
            .insCoName(entry("36", "3", ".01", "Shanktopus!", "SHANKTOPUS"))
            .groupPlanInsuranceCompany(entry("355.3", "4", ".01", "Big Boi!", "BIG BOY"))
            .limitationsLimitationComment(entry("355.32", "5", "2", "Oh my!", "EHRMAHGERD!"))
            .build();
    assertThat(sample).isEqualTo(expected);
  }

  private GetInsEntry entry(
      String file, String ien, String field, String externalVal, String internalVal) {
    return GetInsEntry.builder()
        .fileNumber(file)
        .ien(ien)
        .fieldNumber(field)
        .internalValueRepresentation(internalVal)
        .externalValueRepresentation(externalVal)
        .build();
  }
}
