package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;
import io.micrometer.core.instrument.util.IOUtils;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class VprGetPatientDataResponseTest {

  @SneakyThrows
  @Test
  public void fromResultsDeserializeValidVprGetPatientDataResponse() {
    String invocationResponse =
        IOUtils.toString(getClass().getResourceAsStream("/SampleVitalsResult.xml"));
    RpcInvocationResult invocationResult =
        RpcInvocationResult.builder().vista("673").response(invocationResponse).build();
    assertThat(VprGetPatientData.create().fromResults(List.of(invocationResult)))
        .isEqualTo(VprGetPatientDataSamples.Response.create().response());
  }
}
