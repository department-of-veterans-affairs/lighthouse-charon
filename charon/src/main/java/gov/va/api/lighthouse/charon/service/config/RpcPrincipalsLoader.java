package gov.va.api.lighthouse.charon.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.lighthouse.charon.api.RpcPrincipalConfig;
import gov.va.api.lighthouse.charon.api.RpcPrincipals;
import java.io.File;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor
public class RpcPrincipalsLoader {

  @Bean
  @SneakyThrows
  RpcPrincipals loadPrincipals(@Value("${charon.principals.file}") String principalFile) {
    return RpcPrincipals.builder()
        .config(new ObjectMapper().readValue(new File(principalFile), RpcPrincipalConfig.class))
        .build();
  }
}
