package gov.va.api.lighthouse.charon.models.xobvtestping;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class XobvTestPingTest {
  @Test
  void asDetails() {
    assertThat(XobvTestPing.Request.builder().build().asDetails())
        .isEqualTo(
            RpcDetails.builder().name("XOBV TEST PING").context("XOBV VISTALINK TESTER").build());
    assertThat(XobvTestPing.Request.builder().context(Optional.of("CONTEXT")).build().asDetails())
        .isEqualTo(RpcDetails.builder().name("XOBV TEST PING").context("CONTEXT").build());
    var sample = XobvTestPing.Request.builder().build();
    sample.updateContext(Optional.of("CONTEXT"));
    assertThat(sample.asDetails())
        .isEqualTo(RpcDetails.builder().name("XOBV TEST PING").context("CONTEXT").build());
  }

  @Test
  void fromResults() {
    assertThat(XobvTestPing.create().fromResults(List.of()))
        .isEqualTo(XobvTestPing.Response.builder().resultsByStation(Map.of()).build());
    assertThat(
            XobvTestPing.create()
                .fromResults(
                    List.of(
                        RpcInvocationResult.builder().vista("123").response("payload").build(),
                        RpcInvocationResult.builder()
                            .vista("456")
                            .error(Optional.of("Ew, David!"))
                            .build())))
        .isEqualTo(
            XobvTestPing.Response.builder().resultsByStation(Map.of("123", "payload")).build());
  }
}
