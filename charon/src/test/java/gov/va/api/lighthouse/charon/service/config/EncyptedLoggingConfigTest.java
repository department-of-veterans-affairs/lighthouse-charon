package gov.va.api.lighthouse.charon.service.config;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.autoconfig.encryption.BasicEncryption;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class EncyptedLoggingConfigTest {

  @ParameterizedTest
  @ValueSource(strings = {"", "unset"})
  @NullSource
  void missingEncryptionKeyDisablesEncryption(String key) {
    var bean = new EncyptedLoggingConfig().createEncryptedLogging(key);
    assertThat(bean.encrypt("secret")).doesNotContain("secret");
  }

  @Test
  void specifiedEncryptionKeyEnablesEncryption() {
    String key = "shanktopus";
    var bean = new EncyptedLoggingConfig().createEncryptedLogging(key);
    String aBigSecret = "a big secret";
    var encrypted = bean.encrypt(aBigSecret);
    assertThat(encrypted).doesNotContain("secret");
    var decrypted = BasicEncryption.forKey(key).decrypt(encrypted);
    assertThat(decrypted).isEqualTo(aBigSecret);
  }
}
