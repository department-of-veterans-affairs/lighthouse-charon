package gov.va.api.lighthouse.vistalink.models;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.models.vprgetpatientdata.VprGetPatientData;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class VprGetPatientDataResponseTest {

  @SneakyThrows
  @Test
  public void deserializeValidVprGetPatientDataResponse() {
    String invocationResponse =
        Files.readString(Path.of("src/test/resources/SampleVitalsResult.xml"));
    RpcInvocationResult invocationResult =
        RpcInvocationResult.builder().response(invocationResponse).build();
    assertThat(VprGetPatientData.create().fromResults(List.of(invocationResult)))
        .isEqualTo(VprGetPatientDataSamples.response());
  }
}
