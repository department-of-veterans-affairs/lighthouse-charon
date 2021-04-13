package gov.va.api.lighthouse.charon.service.config;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@Slf4j
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
      "vistalink.configuration=src/test/resources/vistalink.properties",
      "charon.rpc.client-keys=disabled",
      "clinical-authorization-status.access-code=123",
      "clinical-authorization-status.verify-code=987",
      "clinical-authorization-status.application-proxy-user=supershank",
      "clinical-authorization-status.default-menu-option=shank"
    })
public class PathRewriteConfigTest {
  @Autowired TestRestTemplate restTemplate;

  @LocalServerPort private int port;

  @Test
  void pathIsRewritten() {
    assertThat(
        restTemplate.getForObject(
            "http://localhost:" + port + "/charon/rpc/connections", VistalinkProperties.class));
    assertThat(
        restTemplate.getForObject(
            "http://localhost:" + port + "/rpc/connections", VistalinkProperties.class));
  }
}
