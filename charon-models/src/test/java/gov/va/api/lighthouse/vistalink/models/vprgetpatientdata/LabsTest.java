package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.vistalink.api.RpcInvocationResult;
import gov.va.api.lighthouse.vistalink.models.CodeAndNameXmlAttribute;
import gov.va.api.lighthouse.vistalink.models.ValueOnlyXmlAttribute;
import io.micrometer.core.instrument.util.IOUtils;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LabsTest {
  LabsSamples labsSamples = LabsSamples.create();

  @Test
  public static Stream<Arguments> isNotEmpty() {
    return Stream.of(
        Arguments.of(Labs.Lab.builder().build(), false),
        Arguments.of(Labs.Lab.builder().collected(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().comment(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(
            Labs.Lab.builder().facility(CodeAndNameXmlAttribute.of("a", "b")).build(), true),
        Arguments.of(Labs.Lab.builder().groupName(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().high(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().id(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(
            Labs.Lab.builder().interpretation(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().labOrderId(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().localName(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().loinc(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().low(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().performingLab(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().provider(Labs.Provider.builder().build()).build(), true),
        Arguments.of(Labs.Lab.builder().orderId(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().result(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().resulted(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().sample(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(
            Labs.Lab.builder().specimen(CodeAndNameXmlAttribute.of("a", "b")).build(), true),
        Arguments.of(Labs.Lab.builder().status(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().test(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().type(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().units(ValueOnlyXmlAttribute.of("a")).build(), true),
        Arguments.of(Labs.Lab.builder().vuid(ValueOnlyXmlAttribute.of("a")).build(), true));
  }

  @SneakyThrows
  @Test
  void deserialize() {
    var xml = IOUtils.toString(getClass().getResourceAsStream("/SampleLabsResult.xml"));
    var result = RpcInvocationResult.builder().vista("673").response(xml).build();
    assertThat(VprGetPatientData.create().fromResults(List.of(result)))
        .isEqualTo(VprGetPatientDataSamples.Response.create().responseFor(labsSamples.labs()));
  }

  @ParameterizedTest
  @MethodSource
  void isNotEmpty(Labs.Lab lab, boolean expected) {
    assertThat(lab.isNotEmpty()).isEqualTo(expected);
  }

  @Test
  void labsStream() {
    assertThat(VprGetPatientData.Response.Results.builder().build().labStream()).isEmpty();
    var sample =
        VprGetPatientDataSamples.Response.create()
            .resultsByStation(labsSamples.labs())
            .get("673")
            .labStream()
            .collect(Collectors.toList());
    assertThat(sample).isEqualTo(labsSamples.labResults());
  }
}
