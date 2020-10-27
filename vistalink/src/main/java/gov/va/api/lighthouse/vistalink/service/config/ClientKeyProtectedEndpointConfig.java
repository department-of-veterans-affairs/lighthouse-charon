package gov.va.api.lighthouse.vistalink.service.config;

import static gov.va.api.lighthouse.talos.Responses.unauthorizedAsJson;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.lighthouse.talos.ClientKeyProtectedEndpointFilter;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ClientKeyProtectedEndpointConfig {

  @Bean
  FilterRegistrationBean<ClientKeyProtectedEndpointFilter> clientKeyProtectedEndpointFilter(
      @Value("${vistalink.rpc.client-keys.enabled}") String rpcClientKeysEnabled,
      @Value("${vistalink.rpc.client-keys}") String rpcClientKeysCsv) {
    var rpcRequestFilter = new FilterRegistrationBean<ClientKeyProtectedEndpointFilter>();

    if (!Boolean.parseBoolean(rpcClientKeysEnabled)) {
      log.warn(
          "RPC Request Filter is disabled. "
              + "To enable, set vistalink.rpc.client-keys.enabled to true.");
      rpcRequestFilter.setEnabled(false);
    }

    var clientKeys = Arrays.stream(rpcClientKeysCsv.split(",")).collect(Collectors.toList());

    rpcRequestFilter.setFilter(
        ClientKeyProtectedEndpointFilter.builder()
            .clientKeys(clientKeys)
            .name("RPC Request")
            .unauthorizedResponse(unauthorizedResponse())
            .build());

    rpcRequestFilter.addUrlPatterns("/rpc", "/rpc/connections");

    return rpcRequestFilter;
  }

  @SneakyThrows
  private Consumer<HttpServletResponse> unauthorizedResponse() {
    var response =
        RpcResponse.builder()
            .status(RpcResponse.Status.FAILED)
            .message(Optional.of("Unauthorized: Check the client-key header."))
            .build();
    return unauthorizedAsJson(JacksonConfig.createMapper().writeValueAsString(response));
  }
}
