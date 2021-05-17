package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.config.VistalinkProperties;
import gov.va.api.lighthouse.mpi.MpiConfig;
import java.util.List;
import java.util.function.Function;
import org.assertj.core.api.Assertions;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201304UV02Patient;
import org.junit.jupiter.api.BeforeEach;
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

  @Mock MpiConfig config;

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

  @BeforeEach
  void _init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void request1309() {
    var resolver = new MpiVistaNameResolver(properties, config);
    assertThat(resolver.request1309()).isInstanceOf(Function.class);
    Function<String, PRPAIN201310UV02> mockFunction =
        (s) -> {
          return mockResponse;
        };
    var resolverFunctionOverride = new MpiVistaNameResolver(properties, config);
    resolverFunctionOverride.request1309(mockFunction);
    assertThat(resolverFunctionOverride.request1309()).isEqualTo(mockFunction);
  }

  @Test
  void resolveExplodesWhenMultiplePatientsAreFound() {
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
                        .build(),
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
                                                                "100008890^PI^FAKESTATION2^USVHA^A")
                                                            .build()))
                                                .build())
                                        .build())
                                .build())
                        .build()))
            .build();
    Mockito.when(mockResponse.getControlActProcess()).thenReturn(controlActProcess);
    Function<String, PRPAIN201310UV02> mockFunction =
        (s) -> {
          return mockResponse;
        };
    MpiVistaNameResolver resolver = new MpiVistaNameResolver(properties, config);
    resolver.request1309(mockFunction);
    assertThatExceptionOfType(VistaLinkExceptions.UnknownPatient.class)
        .isThrownBy(() -> resolver.resolve(RpcVistaTargets.builder().forPatient("1").build()));
  }

  @Test
  void resolveReturnsConnectionDetails() {
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
    Mockito.when(mockResponse.getControlActProcess()).thenReturn(controlActProcess);
    Function<String, PRPAIN201310UV02> mockFunction =
        (s) -> {
          return mockResponse;
        };
    MpiVistaNameResolver resolver = new MpiVistaNameResolver(properties, config);
    resolver.request1309(mockFunction);
    Assertions.assertThat(resolver.resolve(RpcVistaTargets.builder().forPatient("1").build()))
        .isEqualTo(
            List.of(
                ConnectionDetails.builder()
                    .name("FAKESTATION")
                    .host("fakehost")
                    .port(1337)
                    .divisionIen("0")
                    .build()));
  }

  @Test
  void resolveReturnsEmptyConnectionsWhenNoPatientsAreFound() {
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
                                                                "100005750^PI^NOT_A_STATION^NOPE^N")
                                                            .build()))
                                                .build())
                                        .build())
                                .build())
                        .build()))
            .build();
    Mockito.when(mockResponse.getControlActProcess()).thenReturn(controlActProcess);
    Function<String, PRPAIN201310UV02> mockFunction =
        (s) -> {
          return mockResponse;
        };
    MpiVistaNameResolver resolver = new MpiVistaNameResolver(properties, config);
    resolver.request1309(mockFunction);
    var connections = resolver.resolve(RpcVistaTargets.builder().forPatient("1").build());
    assertThat(connections).isEmpty();
  }

  @Test
  void resolveReturnsExplicitlyIncludedPatientsOnlyWhenNoPatientsAreFound() {
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
                                                                "100005750^PI^NOT_A_STATION^NOPE^N")
                                                            .build()))
                                                .build())
                                        .build())
                                .build())
                        .build()))
            .build();
    Mockito.when(mockResponse.getControlActProcess()).thenReturn(controlActProcess);
    Function<String, PRPAIN201310UV02> mockFunction =
        (s) -> {
          return mockResponse;
        };

    ConnectionDetails in1 =
        ConnectionDetails.builder()
            .divisionIen("0")
            .host("fakehost")
            .name("in1")
            .port(1337)
            .build();
    ConnectionDetails in2 =
        ConnectionDetails.builder()
            .divisionIen("0")
            .host("fakehost")
            .name("in2")
            .port(1337)
            .build();
    VistalinkProperties propertiesWithExtraStations =
        VistalinkProperties.builder()
            .vista(in1)
            .vista(in2)
            .vista(
                ConnectionDetails.builder()
                    .divisionIen("0")
                    .host("fakehost")
                    .name("notin")
                    .port(1337)
                    .build())
            .build();

    MpiVistaNameResolver resolver = new MpiVistaNameResolver(propertiesWithExtraStations, config);
    resolver.request1309(mockFunction);
    var connections =
        resolver.resolve(
            RpcVistaTargets.builder().forPatient("1").include(List.of("in1", "in2")).build());
    assertThat(connections).containsExactlyInAnyOrder(in1, in2);
  }

  @Test
  void targetsForPatientReturnsEmptyListWhenNoPatientIsSpecified() {
    var resolver = new MpiVistaNameResolver(properties, config);
    assertThat(resolver.resolve(RpcVistaTargets.builder().build())).isEmpty();
  }
}
