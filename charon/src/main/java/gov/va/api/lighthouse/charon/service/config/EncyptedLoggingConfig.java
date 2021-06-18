package gov.va.api.lighthouse.charon.service.config;

import static org.apache.commons.lang3.StringUtils.isBlank;

import gov.va.api.health.autoconfig.encryption.BasicEncryption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class EncyptedLoggingConfig {

  @Bean
  EncryptedLogging createEncryptedLogging(
      @Value("${charon.public-web-exception-key}") String encryptionKey) {
    if ("unset".equals(encryptionKey) || isBlank(encryptionKey)) {
      log.info("charon.public-web-exception-key has not been provided, logging will be limited");
      return new DisabledEncryptedLogging();
    } else {
      log.info("encrypted logging enabled");
      return EnabledEncryptedLogging.of(encryptionKey);
    }
  }

  public interface EncryptedLogging {
    String encrypt(String message);
  }

  public static class DisabledEncryptedLogging implements EncryptedLogging {
    @Override
    public String encrypt(String message) {
      return "REDACTED";
    }
  }

  @RequiredArgsConstructor(staticName = "of")
  public static class EnabledEncryptedLogging implements EncryptedLogging {
    private final String encryptionKey;

    @Override
    public String encrypt(String message) {
      return BasicEncryption.forKey(encryptionKey).encrypt(message);
    }
  }
}
