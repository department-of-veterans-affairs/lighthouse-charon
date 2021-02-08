package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import gov.va.api.lighthouse.vistalink.api.RpcDetails;
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

    public VprGetPatientData.Response responseFor(Object result) {
      return VprGetPatientData.Response.builder()
          .resultsByStation(resultsByStation(result))
          .build();
    }

    public Map<String, VprGetPatientData.Response.Results> resultsByStation(Object result) {
      var builder = VprGetPatientData.Response.Results.builder().version("1.13").timeZone("-0500");

      switch (result.getClass().getSimpleName()) {
        case "Vitals":
          builder.vitals((Vitals) result);
          break;
        case "Labs":
          builder.labs((Labs) result);
          break;
      }

      return Map.of("673", builder.build());
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
