package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.vistalink.service.api.RpcVistaTargets;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.api.lighthouse.vistalink.service.config.VistalinkProperties;
import java.util.List;
import java.util.function.Function;
import org.hl7.v3.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MpiVistaNameResolverTest {

  VistalinkProperties properties =
      VistalinkProperties.builder()
          .vista(
              ConnectionDetails.builder()
                  .divisionIen("0")
                  .host("fakehost")
                  .name("FAKESTATION")
                  .port(1337)
                  .build())
          .build();

  @Mock PRPAIN201310UV02 mockResponse;

  PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess =
      PRPAIN201310UV02MFMIMT700711UV01ControlActProcess.builder()
          .subject(
              List.of(
                  PRPAIN201310UV02MFMIMT700711UV01Subject1.builder()
                      .registrationEvent(
                          PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent.builder()
                              .subject1(
                                  PRPAIN201310UV02MFMIMT700711UV01Subject2.builder()
                                      .patient(
                                          PRPAMT201304UV02Patient.builder()
                                              .id(
                                                  List.of(
                                                      II.iIBuilder()
                                                          .extension(
                                                              "100005750^PI^FAKESTATION^USVHA^A")
                                                          .build()))
                                              .build())
                                      .build())
                              .build())
                      .build()))
          .build();

  @Test
  void resolveReturnsConnectionDetails() {
    MockitoAnnotations.initMocks(this);

    Mockito.when(mockResponse.getControlActProcess()).thenReturn(controlActProcess);
    Function<String, PRPAIN201310UV02> mockFunction =
        (s) -> {
          return mockResponse;
        };
    MpiVistaNameResolver resolver = new MpiVistaNameResolver(properties, mockFunction);

    assertThat(resolver.resolve(RpcVistaTargets.builder().forPatient("1").build()))
        .isEqualTo(
            List.of(
                ConnectionDetails.builder()
                    .name("FAKESTATION")
                    .host("fakehost")
                    .port(1337)
                    .divisionIen("0")
                    .build()));
  }
}
