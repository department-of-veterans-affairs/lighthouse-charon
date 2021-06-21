package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class IblhsAmcmsGetInsTest {

  @Test
  void fromResults() {
    assertThat(IblhsAmcmsGetIns.create().fromResults(List.of()).getResultsByStation())
        .isEqualTo(
            IblhsAmcmsGetIns.Response.builder()
                .resultsByStation(Map.of())
                .build()
                .getResultsByStation());
    assertThat(
            IblhsAmcmsGetIns.create()
                .fromResults(
                    List.of(
                        RpcInvocationResult.builder()
                            .vista("666")
                            .error(Optional.of("4311"))
                            .build(),
                        RpcInvocationResult.builder()
                            .vista("888")
                            .response("36^1^.01^Shanktopus!^SHANKTOPUS")
                            .build()))
                .getResultsByStation())
        .isEqualTo(
            IblhsAmcmsGetIns.Response.builder()
                .resultsByStation(
                    Map.of(
                        "888",
                        GetInsRpcResults.builder()
                            .insCoName(
                                GetInsEntry.builder()
                                    .fileNumber("36")
                                    .ien("1")
                                    .fieldNumber(".01")
                                    .externalValueRepresentation("Shanktopus!")
                                    .internalValueRepresentation("SHANKTOPUS")
                                    .build())
                            .build()))
                .build()
                .getResultsByStation());
  }

  @Test
  void requestAsDetails() {
    assertThat(IblhsAmcmsGetIns.Request.builder().icn("it-me").build().asDetails())
        .isEqualTo(
            RpcDetails.builder()
                .name("IBLHS AMCMS GET INS")
                .context("IBLHS AMCMS RPCS")
                .parameters(List.of(RpcDetails.Parameter.builder().string("it-me").build()))
                .build());
  }
}
