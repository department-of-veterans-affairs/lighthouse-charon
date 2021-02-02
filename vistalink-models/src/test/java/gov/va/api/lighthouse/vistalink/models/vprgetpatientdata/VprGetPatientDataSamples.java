package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import gov.va.api.lighthouse.vistalink.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.models.CodeAndNameXmlAttribute;
import gov.va.api.lighthouse.vistalink.models.ValueOnlyXmlAttribute;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VprGetPatientDataSamples {
  @AllArgsConstructor(staticName = "create")
  public static class Response {
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
      return Map.of(
          "673",
          VprGetPatientData.Response.Results.builder()
              .version("1.13")
              .timeZone("-0500")
              .vitalInformation(Vitals.builder().total(1).vitalResults(vitals()).build())
              .build());
    }

    public List<Vitals.Vital> vitals() {
      return List.of(
          Vitals.Vital.builder()
              .entered(ValueOnlyXmlAttribute.builder().value("3110225.110428").build())
              .facility(
                  CodeAndNameXmlAttribute.builder().code("673").name("TAMPA (JAH VAH)").build())
              .location(
                  CodeAndNameXmlAttribute.builder().code("23").name("GENERAL MEDICINE").build())
              .measurements(measurements())
              .taken(ValueOnlyXmlAttribute.builder().value("3100406.14").build())
              .removed(List.of(ValueOnlyXmlAttribute.builder().value("INVALID RECORD").build()))
              .build());
    }
  }

  @AllArgsConstructor(staticName = "create")
  public static class Request {
    public RpcDetails details() {
      return RpcDetails.builder()
          .context("VPR APPLICATION PROXY")
          .name("VPR GET PATIENT DATA")
          .parameters(
              List.of(
                  RpcDetails.Parameter.builder().string("I2-0000").build(),
                  RpcDetails.Parameter.builder().string("vitals").build(),
                  RpcDetails.Parameter.builder().string("").build(),
                  RpcDetails.Parameter.builder().string("").build(),
                  RpcDetails.Parameter.builder().string("1").build(),
                  RpcDetails.Parameter.builder().string("32071").build(),
                  RpcDetails.Parameter.builder().array(Collections.emptyList()).build()))
          .build();
    }

    public VprGetPatientData.Request request() {
      return VprGetPatientData.Request.builder()
          .dfn("I2-0000")
          .type(Set.of(VprGetPatientData.Domains.vitals))
          .max(Optional.of("1"))
          .id(Optional.of("32071"))
          .filter(List.of())
          .build();
    }
  }
}
