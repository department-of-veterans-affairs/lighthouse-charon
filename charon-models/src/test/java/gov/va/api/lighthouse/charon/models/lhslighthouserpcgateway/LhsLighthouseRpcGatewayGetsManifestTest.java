package gov.va.api.lighthouse.charon.models.lhslighthouserpcgateway;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class LhsLighthouseRpcGatewayGetsManifestTest {

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
    assertThat(LhsLighthouseRpcGatewayGetsManifest.create().fromResults(sample))
        .isEqualTo(expected);
  }

  @Test
  void lazyInitialization() {
    // Request
    var sample =
        LhsLighthouseRpcGatewayGetsManifest.Request.builder()
            .file("1")
            .iens("2")
            .fields(List.of("3"))
            .build();
    assertThat(sample.flags()).isEmpty();
  }

  @Test
  void requestAsDetails() {
    var sample =
        LhsLighthouseRpcGatewayGetsManifest.Request.builder()
            .file("2")
            .iens("1")
            .fields(List.of(".01", ".3121*"))
            .flags(
                List.of(
                    LhsLighthouseRpcGatewayGetsManifest.Request.GetsManifestFlags.DONT_RETURN_NULL,
                    LhsLighthouseRpcGatewayGetsManifest.Request.GetsManifestFlags
                        .RETURN_INTERNAL_VALUES,
                    LhsLighthouseRpcGatewayGetsManifest.Request.GetsManifestFlags
                        .RETURN_EXTERNAL_VALUES))
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
                                "api^manifest^gets",
                                "param^FILE^literal^2",
                                "param^IENS^literal^1",
                                "param^FIELDS^literal^.01;.3121*",
                                "param^FLAGS^literal^NIE"))
                        .build()))
            .build();
    assertThat(sample.asDetails()).isEqualTo(expected);
  }
}
