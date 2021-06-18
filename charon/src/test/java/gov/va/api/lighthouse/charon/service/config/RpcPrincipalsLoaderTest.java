package gov.va.api.lighthouse.charon.service.config;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gov.va.api.lighthouse.charon.api.RpcPrincipals;
import org.junit.jupiter.api.Test;

public class RpcPrincipalsLoaderTest {

  @Test
  void loadPrincipals() {
    assertThat(new RpcPrincipalsLoader().loadPrincipals("src/test/resources/principals.json"))
        .isInstanceOf(RpcPrincipals.class);
  }
}
