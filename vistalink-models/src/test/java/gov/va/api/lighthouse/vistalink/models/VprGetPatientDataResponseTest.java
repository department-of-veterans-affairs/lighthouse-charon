package gov.va.api.lighthouse.vistalink.models;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.vistalink.models.vprgetpatientdata.Vital;
import gov.va.api.lighthouse.vistalink.models.vprgetpatientdata.VprGetPatientData;
import java.util.List;
import org.junit.jupiter.api.Test;

public class VprGetPatientDataResponseTest {

  VprGetPatientData.Response.Results results =
      VprGetPatientData.Response.Results.builder()
          .version("1.13")
          .timeZone("-0500")
          .vitals(
              VprGetPatientData.Response.Results.Vitals.builder()
                  .total("1")
                  .vitals(
                      List.of(
                          Vital.builder()
                              .entered(Vital.Entered.builder().value("3110225.110428").build())
                              .facility(
                                  Vital.Facility.builder()
                                      .code("673")
                                      .name("TAMPA (JAH VAH)")
                                      .build())
                              .location(
                                  Vital.Location.builder()
                                      .code("23")
                                      .name("GENERAL MEDICINE")
                                      .build())
                              .measurements(
                                  List.of(
                                      Vital.Measurement.builder()
                                          .id("32071")
                                          .vuid("4500634")
                                          .name("BLOOD PRESSURE")
                                          .value("126/65")
                                          .units("mm[Hg]")
                                          .high("210/110")
                                          .low("100/60")
                                          .build(),
                                      Vital.Measurement.builder()
                                          .id("32075")
                                          .vuid("4688724")
                                          .name("HEIGHT")
                                          .value("73")
                                          .units("in")
                                          .metricValue("185.42")
                                          .metricUnits("cm")
                                          .build(),
                                      Vital.Measurement.builder()
                                          .id("32074")
                                          .vuid("4500636")
                                          .name("PULSE")
                                          .value("66")
                                          .units("/min")
                                          .high("120")
                                          .low("60")
                                          .build(),
                                      Vital.Measurement.builder()
                                          .id("32077")
                                          .vuid("4500637")
                                          .name("PULSE OXIMETRY")
                                          .value("95")
                                          .units("%")
                                          .high("100")
                                          .low("50")
                                          .build(),
                                      Vital.Measurement.builder()
                                          .id("32073")
                                          .vuid("4688725")
                                          .name("RESPIRATION")
                                          .value("16")
                                          .units("/min")
                                          .high("30")
                                          .low("8")
                                          .build(),
                                      Vital.Measurement.builder()
                                          .id("32072")
                                          .vuid("4500638")
                                          .low("100/60")
                                          .name("TEMPERATURE")
                                          .value("98.6")
                                          .units("F")
                                          .metricValue("37.0")
                                          .metricUnits("C")
                                          .high("102")
                                          .low("95")
                                          .build(),
                                      Vital.Measurement.builder()
                                          .id("32076")
                                          .vuid("4500639")
                                          .name("WEIGHT")
                                          .value("190")
                                          .units("lb")
                                          .metricValue("86.18")
                                          .metricUnits("kg")
                                          .bmi("25")
                                          .build()))
                              .taken(Vital.Taken.builder().value("3100406.14").build())
                              .removed(List.of(Vital.Removed.builder().value("9").build()))
                              .build()))
                  .build())
          .build();

  @Test
  public void deserializeValidVprGetPatientDataResponse() {
    assertThat(
            XmlResponseRpc.deserialize(
                "<results version='1.13' timeZone='-0500' >\n"
                    + "<vitals total='1' >\n"
                    + "<vital>\n"
                    + "<entered value='3110225.110428' />\n"
                    + "<facility code='673' name='TAMPA (JAH VAH)' />\n"
                    + "<location code='23' name='GENERAL MEDICINE' />\n"
                    + "<measurements>\n"
                    + "<measurement id='32071' vuid='4500634' name='BLOOD PRESSURE' value='126/65' units='mm[Hg]' high='210/110' low='100/60' />\n"
                    + "<measurement id='32075' vuid='4688724' name='HEIGHT' value='73' units='in' metricValue='185.42' metricUnits='cm' />\n"
                    + "<measurement id='32074' vuid='4500636' name='PULSE' value='66' units='/min' high='120' low='60' />\n"
                    + "<measurement id='32077' vuid='4500637' name='PULSE OXIMETRY' value='95' units='%' high='100' low='50' />\n"
                    + "<measurement id='32073' vuid='4688725' name='RESPIRATION' value='16' units='/min' high='30' low='8' />\n"
                    + "<measurement id='32072' vuid='4500638' name='TEMPERATURE' value='98.6' units='F' metricValue='37.0' metricUnits='C' high='102' low='95' />\n"
                    + "<measurement id='32076' vuid='4500639' name='WEIGHT' value='190' units='lb' metricValue='86.18' metricUnits='kg' bmi='25' />\n"
                    + "</measurements>\n"
                    + "<taken value='3100406.14' />\n"
                    + "<removed value='9' />\n"
                    + "</vital>\n"
                    + "</vitals>\n"
                    + "</results>",
                VprGetPatientData.Response.Results.class))
        .isEqualTo(results);
  }
}
