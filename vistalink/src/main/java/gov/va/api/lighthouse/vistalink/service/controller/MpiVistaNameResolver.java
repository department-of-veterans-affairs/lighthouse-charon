package gov.va.api.lighthouse.vistalink.service.controller;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;

import gov.va.api.lighthouse.mpi.MpiConfig;
import gov.va.api.lighthouse.mpi.PatientIdentifierSegment;
import gov.va.api.lighthouse.mpi.SoapMasterPatientIndexClient;
import gov.va.api.lighthouse.vistalink.service.api.RpcVistaTargets;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.api.lighthouse.vistalink.service.config.VistalinkProperties;
import gov.va.api.lighthouse.vistalink.service.controller.VistaLinkExceptions.NameResolutionException;
import gov.va.api.lighthouse.vistalink.service.controller.VistaLinkExceptions.UnknownPatient;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAMT201304UV02Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "vistalink.resolver", havingValue = "mpi")
@Slf4j
public class MpiVistaNameResolver implements VistaNameResolver {
  @Getter private final MpiConfig mpiConfig;

  @Getter private final VistalinkProperties properties;

  @Setter private Function<String, PRPAIN201310UV02> request1309;

  /**
   * Create a new instance with required properties, mpi configuration, and MPI request. If the
   * request is not specified, default to the SoapMPI request to 1309.
   */
  public MpiVistaNameResolver(
      @Autowired VistalinkProperties properties, @Autowired MpiConfig mpiConfig) {
    this.properties = properties;
    this.mpiConfig = mpiConfig;
    log.info("Accessing MPI at {}", mpiConfig.getUrl());
    log.info("Presenting as {}", mpiConfig().getKeyAlias());
  }

  /** Lazy Getter. */
  public Function<String, PRPAIN201310UV02> request1309() {
    if (request1309 == null) {
      return (icn) -> {
        try {
          return SoapMasterPatientIndexClient.of(mpiConfig).request1309ByIcn(icn);
        } catch (Exception e) {
          throw new NameResolutionException(ErrorCodes.MREQ01, "Failed to request 1309", e);
        }
      };
    }
    return request1309;
  }

  @Override
  public List<ConnectionDetails> resolve(RpcVistaTargets rpcVistaTargets) {
    Set<String> vistas = new HashSet<>();
    properties.checkKnownNames(rpcVistaTargets.include());
    properties.checkKnownNames(rpcVistaTargets.exclude());
    vistas.addAll(rpcVistaTargets.include());
    vistas.addAll(targetsForPatient(rpcVistaTargets));
    vistas.removeAll(rpcVistaTargets.exclude());
    var knownVistas = properties.names();
    vistas.removeIf(s -> !knownVistas.contains(s));
    log.info("Known Vistas: {}", knownVistas);
    return properties().vistas().stream().filter(c -> vistas.contains(c.name())).collect(toList());
  }

  private List<String> targetsForPatient(RpcVistaTargets rpcVistaTargets) {
    String icn = rpcVistaTargets.forPatient();
    if (isBlank(icn)) {
      return List.of();
    }
    PRPAIN201310UV02 response = request1309().apply(icn);
    log.info("Response: {}", response);
    List<PRPAIN201310UV02MFMIMT700711UV01Subject1> maybePatients =
        response.getControlActProcess().getSubject();
    /*
     * With a national icn, we only expect 1 patient. If MPI returns more, we need to kill the
     * response to avoid leaking PII
     */
    if (maybePatients.size() != 1) {
      throw new UnknownPatient(
          ErrorCodes.MRSP01, "Expected one patient in response, got " + maybePatients.size());
    }
    PRPAMT201304UV02Patient patient =
        maybePatients.get(0).getRegistrationEvent().getSubject1().getPatient();
    List<String> stationIds =
        patient.getId().stream()
            .filter(Objects::nonNull)
            .map(II::getExtension)
            .filter(Objects::nonNull)
            .map(PatientIdentifierSegment::parse)
            .filter(PatientIdentifierSegment::isVistaSite)
            .map(PatientIdentifierSegment::assigningLocation)
            .collect(toList());
    log.info("Stations: {}", stationIds);
    return stationIds;
  }

  /** Error codes for name resolution errors. */
  public enum ErrorCodes {
    /** Soap request failure. */
    MREQ01,
    /** Wrong number of patients returned. */
    MRSP01
  }
}
