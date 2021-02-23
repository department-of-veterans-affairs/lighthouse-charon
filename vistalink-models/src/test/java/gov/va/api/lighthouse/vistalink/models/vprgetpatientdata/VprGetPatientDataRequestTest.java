package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gov.va.api.lighthouse.vistalink.models.vprgetpatientdata.VprGetPatientData.Request.PatientId;
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
    var sample = VprGetPatientData.Request.builder().dfn(PatientId.forDfn("dfn")).build();
    assertThat(sample.type()).isEmpty();
    assertThat(sample.start()).isEqualTo(Optional.empty());
    assertThat(sample.stop()).isEqualTo(Optional.empty());
    assertThat(sample.max()).isEqualTo(Optional.empty());
    assertThat(sample.id()).isEqualTo(Optional.empty());
    assertThat(sample.filter()).isEmpty();
  }

  @Test
  void patientId() {
    assertThat(PatientId.forDfn("xxx").toString()).isEqualTo("xxx");
    assertThat(PatientId.forIcn("zzz").toString()).isEqualTo(";zzz");
    assertThat(PatientId.builder().dfn("xxx").icn("zzz").build().toString()).isEqualTo("xxx;zzz");
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> PatientId.builder().build());
  }
}
