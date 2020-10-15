package gov.va.api.lighthouse.vistalink.service.config;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Builder
@Slf4j
public class VistalinkPropertiesConfig {

  @Bean
  @SneakyThrows
  VistalinkProperties load(
      @Value("${vistalink.configuration:vistalink.properties}") String vistalinkProperties) {
    Properties p = new Properties();
    try (var is = new FileInputStream(vistalinkProperties)) {
      p.load(is);
    }
    List<ConnectionDetails> vistalinkDetails = parseProperties(p);
    log.info("Loaded {} vista sites from {}", vistalinkDetails.size(), vistalinkProperties);
    return VistalinkProperties.builder().vistas(vistalinkDetails).build();
  }

  private List<ConnectionDetails> parseProperties(Properties p) {
    return p.entrySet().stream()
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
  }
}
