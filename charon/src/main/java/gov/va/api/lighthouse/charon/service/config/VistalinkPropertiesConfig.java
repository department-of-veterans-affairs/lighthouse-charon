package gov.va.api.lighthouse.charon.service.config;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.FileInputStream;
import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor
@Slf4j
public class VistalinkPropertiesConfig {
  /**
   * Parse the given formatted value into a connection detail. Format is
   * hostname:port:divisionIen:timezone and an illegal argument exception will be thrown if the
   * value cannot be parsed.
   */
  public static ConnectionDetails asConnectionDetails(String name, String value) {
    if (isBlank(value)) {
      throw badValue(name, value, "value is blank");
    }
    var parts = value.split(":", -1);
    if (parts.length != 4) {
      throw badValue(name, value, "incorrect number of parts");
    }
    checkValidZoneId(name, value, parts[3]);
    try {
      return ConnectionDetails.builder()
          .name(name)
          .host(parts[0])
          .port(Integer.parseInt(parts[1]))
          .divisionIen(parts[2])
          .timezone(parts[3])
          .build();
    } catch (NumberFormatException e) {
      throw badValue(name, value, "port is not an integer");
    }
  }

  private static IllegalArgumentException badValue(String name, String value, String message) {
    return new IllegalArgumentException(
        String.format(
            "Cannot parse value %s=\"%s\", expected \"hostname:port:divisionIen:timezone\" (%s)",
            name, value, message));
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private static void checkValidZoneId(String name, String value, String zoneId) {
    try {
      ZoneId.of(zoneId);
    } catch (ZoneRulesException e) {
      throw badValue(
          name,
          value,
          "vistalink.properties: vista-connection string is configured with a bad timezone");
    }
  }

  /**
   * Attempt to parse the given connection detail specification. See {@link
   * #asConnectionDetails(String, String)}. An empty value is returned if the specification cannot
   * be parsed.
   */
  public static Optional<ConnectionDetails> parse(String connectionDetailSpecification) {
    if (connectionDetailSpecification.indexOf(':') == -1) {
      return Optional.empty();
    }
    try {
      return Optional.of(
          asConnectionDetails(connectionDetailSpecification, connectionDetailSpecification));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  @Bean
  @SneakyThrows
  VistalinkProperties load(
      @Value("${vistalink.configuration:vistalink.properties}") String vistalinkProperties) {
    Properties properties = new Properties();
    try (var in = new FileInputStream(vistalinkProperties)) {
      properties.load(in);
    }
    List<ConnectionDetails> vistalinkDetails = parseProperties(properties);
    log.info("Loaded {} vista sites from {}", vistalinkDetails.size(), vistalinkProperties);
    return VistalinkProperties.builder().vistas(vistalinkDetails).build();
  }

  List<ConnectionDetails> parseProperties(Properties p) {
    return p.stringPropertyNames().stream()
        .map(name -> asConnectionDetails(name, p.getProperty(name)))
        .collect(Collectors.toList());
  }
}
