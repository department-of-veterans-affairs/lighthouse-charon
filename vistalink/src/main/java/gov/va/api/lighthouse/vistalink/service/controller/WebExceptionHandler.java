package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequestMapping(produces = {"application/json"})
public class WebExceptionHandler {

  @SneakyThrows
  private RpcResponse failedResponseFor(String message) {
    RpcResponse response =
        RpcResponse.builder()
            .status(RpcResponse.Status.FAILED)
            .message(Optional.ofNullable(message))
            .build();
    log.error("Response: {}", JacksonConfig.createMapper().writeValueAsString(response));
    return response;
  }

  @ExceptionHandler({HttpMessageConversionException.class, InvalidRequest.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public RpcResponse handleBadRequestBody(Exception e, HttpServletRequest request) {
    return failedResponseFor("Failed to read request body.");
  }

  @ExceptionHandler({VistaLoginException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public RpcResponse handleFailedLogin(Exception e, HttpServletRequest request) {
    return failedResponseFor("Failed to login.");
  }
}
