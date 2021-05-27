package gov.va.api.lighthouse.charon.tests;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.SentinelProperties;
import gov.va.api.health.sentinel.ServiceDefinition;
import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcDetails.Parameter;
import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.BooleanUtils;

/** System Definitions for each environment. */
@UtilityClass
public class SystemDefinitions {
  /** Return the applicable system definition for the current environment. */
  static SystemDefinition get() {
    switch (Environment.get()) {
      case LOCAL:
        return local();
      case QA:
        return qa();
      case STAGING:
        return staging();
      case PROD:
        return production();
      case STAGING_LAB:
        return stagingLab();
      case LAB:
        return lab();
      default:
        throw new IllegalArgumentException(
            "Unsupported sentinel environment: " + Environment.get());
    }
  }

  private static SystemDefinition lab() {
    String url = "https://blue.lab.lighthouse.va.gov";
    return SystemDefinition.builder()
        .authorizedClinicalUser(authorizedClinicalUser())
        .charon(serviceDefinition("charon", url, 443, "/charon/"))
        .clientKey(Optional.ofNullable(System.getProperty("client-key")))
        .testRpcs(rpcs())
        .avCodePrincipal(avCodeRpcPrincipal())
        .testTargets(rpcTargets())
        .vistaSite(systemPropertyOrEnvVar("vista.site", "673"))
        .isVistaAvailable(isVistaAvailable())
        .build();
  }

  private static SystemDefinition local() {
    String url = "http://localhost";
    // Client-Key is enabled for Health-Check IT.
    // Static client-key is being passed here for other tests to use.
    return SystemDefinition.builder()
        .authorizedClinicalUser(authorizedClinicalUser())
        .charon(serviceDefinition("charon", url, 8050, "/"))
        .clientKey(Optional.of(System.getProperty("client-key", "~shanktopus~")))
        .testRpcs(rpcs())
        .avCodePrincipal(avCodeRpcPrincipal())
        .testTargets(rpcTargets())
        .vistaSite(systemPropertyOrEnvVar("vista.site", "673"))
        .isVistaAvailable(isVistaAvailable())
        .build();
  }

  private static SystemDefinition production() {
    String url = "https://blue.production.lighthouse.va.gov";
    return SystemDefinition.builder()
        .authorizedClinicalUser(authorizedClinicalUser())
        .charon(serviceDefinition("charon", url, 443, "/charon/"))
        .clientKey(Optional.ofNullable(System.getProperty("client-key")))
        .testRpcs(rpcs())
        .avCodePrincipal(avCodeRpcPrincipal())
        .testTargets(rpcTargets())
        .vistaSite(systemPropertyOrEnvVar("vista.site", "673"))
        .isVistaAvailable(isVistaAvailable())
        .build();
  }

  private static SystemDefinition qa() {
    String url = "https://blue.qa.lighthouse.va.gov";
    return SystemDefinition.builder()
        .authorizedClinicalUser(authorizedClinicalUser())
        .charon(serviceDefinition("charon", url, 443, "/charon/"))
        .clientKey(Optional.ofNullable(System.getProperty("client-key")))
        .testRpcs(rpcs())
        .avCodePrincipal(avCodeRpcPrincipal())
        .testTargets(rpcTargets())
        .vistaSite(systemPropertyOrEnvVar("vista.site", "673"))
        .isVistaAvailable(isVistaAvailable())
        .build();
  }

  private static RpcVistaTargets rpcTargets() {
    return RpcVistaTargets.builder().forPatient("ignored-for-now").build();
  }

  private static TestRpcs rpcs() {
    return TestRpcs.builder()
        .pingRpc(
            RpcDetails.builder().context("XOBV VISTALINK TESTER").name("XOBV TEST PING").build())
        .stringRequestRpc(
            RpcDetails.builder()
                .context("XOBV VISTALINK TESTER")
                .name("XOBV TEST STRING")
                .parameters(List.of(Parameter.builder().string("SHANKTOPUS GO!").build()))
                .build())
        .globalArrayRequestRpc(
            RpcDetails.builder()
                .context("XOBV VISTALINK TESTER")
                .name("XOBV TEST GLOBAL ARRAY")
                .parameters(
                    List.of(Parameter.builder().array(List.of("GLOBAL", "SHANKTOPUS")).build()))
                .build())
        .localArrayRequestRpc(
            RpcDetails.builder()
                .context("XOBV VISTALINK TESTER")
                .name("XOBV TEST LOCAL ARRAY")
                .parameters(
                    List.of(Parameter.builder().array(List.of("LOCAL", "SHANKTOPUS")).build()))
                .build())
        .build();
  }

  private static ServiceDefinition serviceDefinition(
      String name, String url, int port, String apiPath) {
    return ServiceDefinition.builder()
        .url(SentinelProperties.optionUrl(name, url))
        .port(port)
        .apiPath(SentinelProperties.optionApiPath(name, apiPath))
        .accessToken(Optional::empty)
        .build();
  }

  private static SystemDefinition staging() {
    String url = "https://blue.staging.lighthouse.va.gov";
    return SystemDefinition.builder()
        .authorizedClinicalUser(authorizedClinicalUser())
        .charon(serviceDefinition("charon", url, 443, "/charon/"))
        .clientKey(Optional.ofNullable(System.getProperty("client-key")))
        .testRpcs(rpcs())
        .avCodePrincipal(avCodeRpcPrincipal())
        .testTargets(rpcTargets())
        .vistaSite(systemPropertyOrEnvVar("vista.site", "673"))
        .isVistaAvailable(isVistaAvailable())
        .build();
  }

  private static SystemDefinition stagingLab() {
    String url = "https://blue.staging-lab.lighthouse.va.gov";
    return SystemDefinition.builder()
        .authorizedClinicalUser(authorizedClinicalUser())
        .charon(serviceDefinition("charon", url, 443, "/charon/"))
        .clientKey(Optional.ofNullable(System.getProperty("client-key")))
        .testRpcs(rpcs())
        .avCodePrincipal(avCodeRpcPrincipal())
        .testTargets(rpcTargets())
        .vistaSite(systemPropertyOrEnvVar("vista.site", "673"))
        .isVistaAvailable(isVistaAvailable())
        .build();
  }

  private SiteDuzPair authorizedClinicalUser() {
    return SiteDuzPair.builder()
        .site(systemPropertyOrEnvVar("vista.authorized-clinical-user.site", "673"))
        .duz(systemPropertyOrEnvVar("vista.authorized-clinical-user.duz", "1"))
        .build();
  }

  private RpcPrincipal avCodeRpcPrincipal() {
    return RpcPrincipal.standardUserBuilder()
        .accessCode(systemPropertyOrEnvVar("vista.standard-user.access-code", "not-set"))
        .verifyCode(systemPropertyOrEnvVar("vista.standard-user.verify-code", "not-set"))
        .build();
  }

  private boolean isVistaAvailable() {
    return BooleanUtils.toBoolean(systemPropertyOrEnvVar("vista.is-available", "false"));
  }

  private String systemPropertyOrEnvVar(String property, String defaultValue) {
    var value = System.getProperty(property);
    if (value == null) {
      value =
          System.getenv(property.replace('.', '_').replace('-', '_').toUpperCase(Locale.ENGLISH));
    }
    return value == null ? defaultValue : value;
  }

  /** Site and DUZ. */
  @Value
  @Builder
  public static class SiteDuzPair {
    String site;

    String duz;
  }
}
