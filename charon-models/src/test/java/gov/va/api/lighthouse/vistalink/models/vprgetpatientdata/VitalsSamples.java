package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import gov.va.api.lighthouse.vistalink.models.CodeAndNameXmlAttribute;
import gov.va.api.lighthouse.vistalink.models.ValueOnlyXmlAttribute;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "create")
public class VitalsSamples {
  public BloodPressure bloodPressure() {
    return BloodPressure.builder()
        .diastolic(
            BloodPressure.BloodPressureMeasurement.builder()
                .high("110")
                .low("60")
                .units("mm[Hg]")
                .value("65")
                .build())
        .systolic(
            BloodPressure.BloodPressureMeasurement.builder()
                .high("210")
                .low("100")
                .units("mm[Hg]")
                .value("126")
                .build())
        .build();
  }

  public List<Vitals.Measurement> measurements() {
    return List.of(
        Vitals.Measurement.builder()
            .id("32071")
            .vuid("4500634")
            .name("BLOOD PRESSURE")
            .value("126/65")
            .units("mm[Hg]")
            .high("210/110")
            .low("100/60")
            .build(),
        Vitals.Measurement.builder()
            .id("32075")
            .vuid("4688724")
            .name("HEIGHT")
            .value("73")
            .units("in")
            .metricValue("185.42")
            .metricUnits("cm")
            .build(),
        Vitals.Measurement.builder()
            .id("32074")
            .vuid("4500636")
            .name("PULSE")
            .value("66")
            .units("/min")
            .high("120")
            .low("60")
            .build(),
        Vitals.Measurement.builder()
            .id("32077")
            .vuid("4500637")
            .name("PULSE OXIMETRY")
            .value("95")
            .units("%")
            .high("100")
            .low("50")
            .build(),
        Vitals.Measurement.builder()
            .id("32073")
            .vuid("4688725")
            .name("RESPIRATION")
            .value("16")
            .units("/min")
            .high("30")
            .low("8")
            .build(),
        Vitals.Measurement.builder()
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
            .qualifiers(qualifiers())
            .build(),
        Vitals.Measurement.builder()
            .id("32076")
            .vuid("4500639")
            .name("WEIGHT")
            .value("190")
            .units("lb")
            .metricValue("86.18")
            .metricUnits("kg")
            .bmi("25")
            .build());
  }

  public List<Vitals.Qualifier> qualifiers() {
    return List.of(Vitals.Qualifier.builder().name("ORAL").vuid("4500642").build());
  }

  public VprGetPatientData.Response response() {
    return VprGetPatientData.Response.builder().resultsByStation(resultsByStation()).build();
  }

  public Map<String, VprGetPatientData.Response.Results> resultsByStation() {
    return VprGetPatientDataSamples.Response.create()
        .resultsByStation(Vitals.builder().total(1).vitalResults(vitals()).build());
  }

  public List<Vitals.Vital> vitals() {
    return List.of(
        Vitals.Vital.builder()
            .entered(ValueOnlyXmlAttribute.builder().value("3110225.110428").build())
            .facility(CodeAndNameXmlAttribute.builder().code("673").name("TAMPA (JAH VAH)").build())
            .location(CodeAndNameXmlAttribute.builder().code("23").name("GENERAL MEDICINE").build())
            .measurements(measurements())
            .taken(ValueOnlyXmlAttribute.builder().value("3100406.14").build())
            .removed(List.of(ValueOnlyXmlAttribute.builder().value("INVALID RECORD").build()))
            .build());
  }
}
