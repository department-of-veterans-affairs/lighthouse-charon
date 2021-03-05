package gov.va.api.lighthouse.vistalink.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.lighthouse.vistalink.api.RpcDetails;
import gov.va.api.lighthouse.vistalink.api.RpcPrincipal;
import gov.va.api.lighthouse.vistalink.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.api.RpcResponse.Status;
import gov.va.api.lighthouse.vistalink.api.RpcVistaTargets;
import gov.va.api.lighthouse.vistalink.service.config.VistalinkProperties;
import gov.va.api.lighthouse.vistalink.service.controller.UnrecoverableVistalinkExceptions.BadRpcContext;
import gov.va.api.lighthouse.vistalink.service.controller.VistaLinkExceptions.NameResolutionException;
import gov.va.api.lighthouse.vistalink.service.controller.VistaLinkExceptions.UnknownPatient;
import gov.va.api.lighthouse.vistalink.service.controller.VistaLinkExceptions.UnknownVista;
import java.lang.reflect.Method;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;
import javax.security.auth.login.LoginException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

@Slf4j
public class WebExceptionHandlerTest {
  RpcExecutor executor = mock(RpcExecutor.class);

  private RpcController controller =
      new RpcController(executor, VistalinkProperties.builder().build());

  private WebExceptionHandler exceptionHandler = new WebExceptionHandler();

  public static Stream<Arguments> expectStatus() {
    return Stream.of(
        arguments(
            HttpStatus.BAD_REQUEST, Status.FAILED, new HttpMessageConversionException("FUGAZI")),
        arguments(HttpStatus.BAD_REQUEST, Status.FAILED, new InvalidRequest("FUGAZI")),
        arguments(HttpStatus.UNAUTHORIZED, Status.FAILED, new LoginException("FUGAZI")),
        arguments(HttpStatus.REQUEST_TIMEOUT, Status.FAILED, new TimeoutException("FUGAZI")),
        arguments(HttpStatus.BAD_REQUEST, Status.FAILED, new UnknownVista("FUGAZI")),
        arguments(
            HttpStatus.BAD_REQUEST,
            Status.VISTA_RESOLUTION_FAILURE,
            new UnknownPatient(Fugazi.FUGAZI, "FUGAZI")),
        arguments(
            HttpStatus.INTERNAL_SERVER_ERROR,
            Status.VISTA_RESOLUTION_FAILURE,
            new NameResolutionException(Fugazi.FUGAZI, null, null)),
        arguments(
            HttpStatus.FORBIDDEN,
            Status.FAILED,
            new BadRpcContext("FUGAZI", new Throwable("FUGAZI"))));
  }

  private ExceptionHandlerExceptionResolver createExceptionResolver() {
    ExceptionHandlerExceptionResolver exceptionResolver =
        new ExceptionHandlerExceptionResolver() {
          @Override
          protected ServletInvocableHandlerMethod getExceptionHandlerMethod(
              HandlerMethod handlerMethod, Exception ex) {
            Method method =
                new ExceptionHandlerMethodResolver(WebExceptionHandler.class).resolveMethod(ex);
            assertThat(method).isNotNull();
            return new ServletInvocableHandlerMethod(exceptionHandler, method);
          }
        };
    exceptionResolver
        .getMessageConverters()
        .add(new MappingJackson2HttpMessageConverter(JacksonConfig.createMapper()));
    return exceptionResolver;
  }

  @SneakyThrows
  @ParameterizedTest
  @MethodSource
  void expectStatus(HttpStatus httpStatus, Status responseStatus, Exception e) {
    when(executor.execute(any()))
        .thenAnswer(
            i -> {
              throw e;
            });
    MockMvc mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setHandlerExceptionResolvers(createExceptionResolver())
            .setMessageConverters(
                new MappingJackson2HttpMessageConverter(JacksonConfig.createMapper()))
            .build();

    var body =
        RpcRequest.builder()
            .principal(RpcPrincipal.builder().accessCode("whatever").verifyCode("whatever").build())
            .target(RpcVistaTargets.builder().forPatient("1234v5678").build())
            .rpc(
                RpcDetails.builder()
                    .name("XOBV TEST PING")
                    .context("XOBV VISTALINK TESTER")
                    .build())
            .build();

    var response =
        mockMvc
            .perform(
                post("/rpc")
                    .contentType("application/json")
                    .content(JacksonConfig.createMapper().writeValueAsString(body)))
            .andExpect(status().is(httpStatus.value()))
            .andExpect(jsonPath("status", equalTo(responseStatus.toString())))
            .andReturn()
            .getResponse();
    log.error("response: {}", response.getContentAsString());
  }

  enum Fugazi {
    FUGAZI;
  }
}
