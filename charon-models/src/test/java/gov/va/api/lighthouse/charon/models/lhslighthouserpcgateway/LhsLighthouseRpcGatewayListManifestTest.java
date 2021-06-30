package gov.va.api.lighthouse.charon.models.lhslighthouserpcgateway;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class LhsLighthouseRpcGatewayListManifestTest {

  @Test
  void fromResults() {
    var sample =
        List.of(
            RpcInvocationResult.builder()
                .vista("777")
                .response(
                    "{\"results\":[{\"fields\":{"
                        + "\"#.01\":{\"ext\":\"BCBS OF FL\",\"in\":4},\"#.18\":{\"ext\":\"BCBS OF FL\",\"in\":87},"
                        + "\"#.2\":{\"ext\":\"PRIMARY\",\"in\":1},\"#3\":{\"ext\":\"JAN 01, 2025\",\"in\":3250101},"
                        + "\"#4.03\":{\"ext\":\"SPOUSE\",\"in\":\"01\"},"
                        + "\"#7.02\":{\"ext\":\"R50797108\",\"in\":\"R50797108\"},"
                        + "\"#8\":{\"ext\":\"JAN 12, 1992\",\"in\":2920112}},\"file\":2.312,\"ien\":\"1,69,\"}]}")
                .build(),
            RpcInvocationResult.builder()
                .vista("666")
                .error(Optional.of("Big Oof!"))
                .response("Nah!")
                .build());
    var expected =
        LhsLighthouseRpcGatewayResponse.builder()
            .resultsByStation(
                Map.of(
                    "777",
                    LhsLighthouseRpcGatewayResponse.Results.builder()
                        .results(
                            List.of(
                                LhsLighthouseRpcGatewayResponse.FilemanEntry.builder()
                                    .file("2.312")
                                    .ien("1,69,")
                                    .fields(
                                        Map.of(
                                            "#.01",
                                                LhsLighthouseRpcGatewayResponse.Values.of(
                                                    "BCBS OF FL", "4"),
                                            "#.18",
                                                LhsLighthouseRpcGatewayResponse.Values.of(
                                                    "BCBS OF FL", "87"),
                                            "#.2",
                                                LhsLighthouseRpcGatewayResponse.Values.of(
                                                    "PRIMARY", "1"),
                                            "#3",
                                                LhsLighthouseRpcGatewayResponse.Values.of(
                                                    "JAN 01, 2025", "3250101"),
                                            "#4.03",
                                                LhsLighthouseRpcGatewayResponse.Values.of(
                                                    "SPOUSE", "01"),
                                            "#7.02",
                                                LhsLighthouseRpcGatewayResponse.Values.of(
                                                    "R50797108", "R50797108"),
                                            "#8",
                                                LhsLighthouseRpcGatewayResponse.Values.of(
                                                    "JAN 12, 1992", "2920112")))
                                    .build()))
                        .build()))
            .build();
    assertThat(LhsLighthouseRpcGatewayListManifest.create().fromResults(sample))
        .isEqualTo(expected);
  }

  @Test
  void lazyInitialization() {
    // Request
    var sample = LhsLighthouseRpcGatewayListManifest.Request.builder().file("1").build();
    assertThat(sample.iens()).isEmpty();
    assertThat(sample.fields()).isEmpty();
    assertThat(sample.flags()).isEmpty();
    assertThat(sample.number()).isEmpty();
    assertThat(sample.from()).isEmpty();
    assertThat(sample.part()).isEmpty();
    assertThat(sample.index()).isEmpty();
    assertThat(sample.screen()).isEmpty();
    assertThat(sample.id()).isEmpty();
  }

  @Test
  void requestAsDetails() {
    var sample =
        LhsLighthouseRpcGatewayListManifest.Request.builder()
            .file("2")
            .iens(Optional.of("1"))
            .fields(List.of(".01", ".3121*"))
            .flags(
                List.of(
                    LhsLighthouseRpcGatewayListManifest.Request.ListManifestFlags
                        .RETURN_INTERNAL_VALUES,
                    LhsLighthouseRpcGatewayListManifest.Request.ListManifestFlags.IGNORE_ERRORS))
            .number(Optional.of("15"))
            .from(
                Optional.of(
                    LhsLighthouseRpcGatewayListManifest.Request.From.builder()
                        .name("NAME")
                        .ien("1")
                        .build()))
            .part(Optional.of("5"))
            .index(Optional.of("2"))
            .screen(Optional.of("3"))
            .id(Optional.of("4"))
            .build();
    var expected =
        RpcDetails.builder()
            .name(LhsLighthouseRpcGatewayListManifest.RPC_NAME)
            .context("LHS RPC CONTEXT")
            .parameters(
                List.of(
                    RpcDetails.Parameter.builder()
                        .array(
                            List.of(
                                "debugmode^1",
                                "api^manifest^list",
                                "param^FILE^literal^2",
                                "param^IENS^literal^1",
                                "param^FIELDS^literal^.01;.3121*",
                                "param^FLAGS^literal^PIE",
                                "param^NUMBER^literal^15",
                                "param^FROM^list^1^NAME",
                                "param^FROM^list^2^1",
                                "param^FROM^list^IEN^1",
                                "param^PART^list^1^5",
                                "param^INDEX^literal^2",
                                "param^SCREEN^literal^3",
                                "param^ID^literal^4"))
                        .build()))
            .build();
    assertThat(sample.asDetails()).isEqualTo(expected);
  }
}
