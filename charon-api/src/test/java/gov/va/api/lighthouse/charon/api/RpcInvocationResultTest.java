package gov.va.api.lighthouse.charon.api;

import static gov.va.api.lighthouse.charon.api.RoundTrip.assertRoundTrip;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

public class RpcInvocationResultTest {
  @Test
  void lazyInitialization() {
    assertThat(RpcInvocationResult.builder().build().error()).isEqualTo(Optional.empty());
  }

  @Test
  void roundTrip() {
    var sample =
        RpcInvocationResult.builder()
            .vista("1")
            .response("Sample Response")
            .error(Optional.of("Sample Error"))
            .build();
    assertRoundTrip(sample);
  }
}
