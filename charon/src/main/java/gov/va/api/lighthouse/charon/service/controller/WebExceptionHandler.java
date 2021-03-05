package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import gov.va.api.lighthouse.charon.api.RpcResponse.Status;
import gov.va.api.lighthouse.charon.service.controller.VistaLinkExceptions.NameResolutionException;
import gov.va.api.lighthouse.charon.service.controller.VistaLinkExceptions.UnknownPatient;
import gov.va.api.lighthouse.charon.service.controller.VistaLinkExceptions.UnknownVista;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import javax.security.auth.login.LoginException;
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
    log.error("Bad request", e);
    return failedResponseFor("Failed to read request body.");
  }

  @ExceptionHandler({UnrecoverableVistalinkExceptions.BadRpcContext.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public RpcResponse handleBadRpcContext(Exception e, HttpServletRequest request) {
    log.error("Bad RPC Context", e);
    return failedResponseFor("RPC is not registered to the chosen RPC context.");
  }

  @ExceptionHandler({LoginException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public RpcResponse handleFailedLogin(Exception e, HttpServletRequest request) {
    log.error("Login failed", e);
    return failedResponseFor("Failed to login.");
  }

  @ExceptionHandler({NameResolutionException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public RpcResponse handleNameResolutionException(Exception e, HttpServletRequest request) {
    log.error("Name resolution exception", e);
    return nameResolutionResponse(e);
  }

  @ExceptionHandler({TimeoutException.class})
  @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
  public RpcResponse handleRequestTimeout(Exception e, HttpServletRequest request) {
    return failedResponseFor("Request timed out.");
  }

  @ExceptionHandler({UnknownPatient.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public RpcResponse handleUnknownPatient(Exception e, HttpServletRequest request) {
    log.error("Unknown patient", e);
    return nameResolutionResponse(e);
  }

  @ExceptionHandler({UnknownVista.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public RpcResponse handleUnknownVistaIncludes(Exception e, HttpServletRequest request) {
    return failedResponseFor("Unknown vista site specified: " + e.getMessage());
  }

  private RpcResponse nameResolutionResponse(Exception e) {
    String message = null;
    if (e instanceof NameResolutionException) {
      message = ((NameResolutionException) e).publicErrorCode();
    }
    return RpcResponse.builder()
        .status(Status.VISTA_RESOLUTION_FAILURE)
        .message(Optional.ofNullable(message))
        .build();
  }
}
