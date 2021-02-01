package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class VprGetPatientDataRequestTest {

  @SneakyThrows
  @Test
  public void asDetailsSuccessfullyBuildsRpcDetails() {
    VprGetPatientDataSamples.Request sampleRequest = VprGetPatientDataSamples.Request.create();
    assertThat(sampleRequest.request().asDetails()).isEqualTo(sampleRequest.details());
  }

  @Test
  void lazyInitializers() {
    var sample = VprGetPatientData.Request.builder().dfn("dfn").build();
    assertThat(sample.type()).isEmpty();
    assertThat(sample.max()).isEqualTo(Optional.empty());
    assertThat(sample.id()).isEqualTo(Optional.empty());
    assertThat(sample.filter()).isEmpty();
  }
}
