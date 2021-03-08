package gov.va.api.lighthouse.charon.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class RpcResponseTest {
  @Test
  void lazyInitialization() {
    var sample = RpcResponse.builder().build();
    assertThat(sample.message()).isEqualTo(Optional.empty());
    assertThat(sample.results()).isEmpty();
  }

  @Test
  void roundTrip() {
    var sample =
        RpcResponse.builder()
            .status(RpcResponse.Status.OK)
            .results(List.of(RpcInvocationResult.builder().build()))
            .message(Optional.of("Message"))
            .build();
  }
}
