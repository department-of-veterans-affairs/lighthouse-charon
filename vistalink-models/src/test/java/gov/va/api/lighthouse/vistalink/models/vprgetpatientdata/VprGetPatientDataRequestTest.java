package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class VprGetPatientDataRequestTest {

  @SneakyThrows
  @Test
  public void asDetailsSuccessfullyBuildsRpcDetails() {
    VprGetPatientDataSamples.Request sampleRequest = VprGetPatientDataSamples.Request.create();
    assertThat(sampleRequest.request().asDetails()).isEqualTo(sampleRequest.details());
  }
}
