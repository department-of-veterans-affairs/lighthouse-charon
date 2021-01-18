package gov.va.api.lighthouse.vistalink.api;

import static gov.va.api.lighthouse.vistalink.api.RoundTrip.assertRoundTrip;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

public class RpcVistaTargetsTest {
  @Test
  void lazyInitialization() {
    var sample = RpcVistaTargets.builder().forPatient("p1").build();
    assertThat(sample.exclude()).isEmpty();
    assertThat(sample.include()).isEmpty();
  }

  @Test
  void roundTrip() {
    var sample =
        RpcVistaTargets.builder()
            .forPatient("p1")
            .exclude(List.of("1"))
            .include(List.of("2"))
            .build();
    assertRoundTrip(sample);
  }

  @Test
  void validation() {
    assertThat(violationsOf(RpcVistaTargets.builder().build())).isNotEmpty();
    assertThat(violationsOf(RpcVistaTargets.builder().forPatient("p1").build())).isEmpty();
    assertThat(violationsOf(RpcVistaTargets.builder().include(List.of("1")).build())).isEmpty();
  }

  private <T> Set<ConstraintViolation<T>> violationsOf(T object) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    return factory.getValidator().validate(object);
  }
}
