package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;
import io.micrometer.core.instrument.util.IOUtils;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class VprGetPatientDataResponseTest {
  VprGetPatientDataSamples.Response samples = VprGetPatientDataSamples.Response.create();

  @Test
  public void bloodPressure() {
    assertThat(samples.measurements().get(0).asBloodPressure())
        .isEqualTo(Optional.of(samples.bloodPressure()));
    assertThat(samples.measurements().get(1).asBloodPressure()).isEqualTo(Optional.empty());
  }

  @SneakyThrows
  @Test
  public void fromResultsDeserializeValidVprGetPatientDataResponse() {
    String invocationResponse =
        IOUtils.toString(getClass().getResourceAsStream("/SampleVitalsResult.xml"));
    RpcInvocationResult invocationResult =
        RpcInvocationResult.builder().vista("673").response(invocationResponse).build();
    assertThat(VprGetPatientData.create().fromResults(List.of(invocationResult)))
        .isEqualTo(samples.response());
  }

  @Test
  void vitalStream() {
    assertThat(VprGetPatientData.Response.Results.builder().build().vitalStream()).isEmpty();
    assertThat(
            samples
                .response()
                .resultsByStation()
                .get("673")
                .vitalStream()
                .collect(Collectors.toList()))
        .isEqualTo(samples.vitals());
  }
}
