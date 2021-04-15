package gov.va.api.lighthouse.charon.models.lhscheckoptionaccess;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class LhsCheckOptionAccessTest {
  @Test
  void asDetails() {
    assertThat(
            LhsCheckOptionAccess.Request.builder()
                .duz("DUZ")
                .menuOption("MENUOPTION")
                .build()
                .asDetails())
        .isEqualTo(
            RpcDetails.builder()
                .name("LHS CHECK OPTION ACCESS")
                .context("LHS RPC CONTEXT")
                .parameters(
                    List.of(
                        RpcDetails.Parameter.builder().string("DUZ").build(),
                        RpcDetails.Parameter.builder().string("MENUOPTION").build()))
                .build());
  }

  @Test
  void fromResults() {
    assertThat(LhsCheckOptionAccess.create().fromResults(List.of()))
        .isEqualTo(LhsCheckOptionAccess.Response.builder().resultsByStation(Map.of()).build());
    assertThat(
            LhsCheckOptionAccess.create()
                .fromResults(
                    List.of(
                        RpcInvocationResult.builder().vista("777").response("1^111").build(),
                        RpcInvocationResult.builder()
                            .vista("888")
                            .error(Optional.of("Ew, David!"))
                            .build())))
        .isEqualTo(
            LhsCheckOptionAccess.Response.builder()
                .resultsByStation(Map.of("777", "1^111"))
                .build());
  }
}
