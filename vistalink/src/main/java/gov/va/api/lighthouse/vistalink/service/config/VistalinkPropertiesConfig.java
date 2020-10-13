package gov.va.api.lighthouse.vistalink.service.config;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Builder
@Slf4j
public class VistalinkPropertiesConfig {

  @Bean
  VistalinkProperties load(
      @Value("${vistalink.properties.file:vistalink.properties}") String vistalinkProperties) {
    Properties p = new Properties();
    try (var is = getClass().getClassLoader().getResourceAsStream(vistalinkProperties)) {
      p.load(is);
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "The vistalink.properties file cannot be found and is required.");
    }
    List<ConnectionDetails> vistalinkDetails =
        p.entrySet().stream()
            .map(
                e -> {
                  var parts = e.getValue().toString().split(":", -1);
                  if (parts.length != 3) {
                    throw new IllegalArgumentException("Cannot parse vistalink.properties.");
                  }
                  return ConnectionDetails.builder()
                      .host(parts[0])
                      .port(Integer.parseInt(parts[1]))
                      .divisionIen(parts[2])
                      .build();
                })
            .collect(Collectors.toList());
    log.info("Loaded {} vista sites from {}", vistalinkDetails.size(), vistalinkProperties);
    return VistalinkProperties.builder().vistas(vistalinkDetails).build();
  }
}
