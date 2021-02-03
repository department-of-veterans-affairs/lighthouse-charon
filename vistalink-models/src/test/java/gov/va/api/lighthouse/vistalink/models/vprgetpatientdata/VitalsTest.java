package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.vistalink.models.CodeAndNameXmlAttribute;
import gov.va.api.lighthouse.vistalink.models.ValueOnlyXmlAttribute;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class VitalsTest {

  public static Stream<Arguments> isNotEmpty() {
    return Stream.of(
        Arguments.of(Vitals.Vital.builder().build(), false),
        Arguments.of(
            Vitals.Vital.builder().entered(ValueOnlyXmlAttribute.builder().build()).build(), true),
        Arguments.of(
            Vitals.Vital.builder().facility(CodeAndNameXmlAttribute.builder().build()).build(),
            true),
        Arguments.of(
            Vitals.Vital.builder().location(CodeAndNameXmlAttribute.builder().build()).build(),
            true),
        Arguments.of(Vitals.Vital.builder().measurements(List.of()).build(), true),
        Arguments.of(
            Vitals.Vital.builder()
                .removed(List.of(ValueOnlyXmlAttribute.builder().build()))
                .build(),
            true),
        Arguments.of(
            Vitals.Vital.builder().taken(ValueOnlyXmlAttribute.builder().build()).build(), true),
        Arguments.of(VprGetPatientDataSamples.Response.create().vitals().get(0), true));
  }

  @ParameterizedTest
  @MethodSource
  void isNotEmpty(Vitals.Vital vital, boolean expected) {
    assertThat(vital.isNotEmpty()).isEqualTo(expected);
  }
}
