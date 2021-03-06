package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.api.RpcInvocationResult;
import gov.va.api.lighthouse.charon.api.RpcPrincipal;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import gov.va.api.lighthouse.charon.api.RpcResponse.Status;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.controller.UnrecoverableVistalinkExceptions.LoginFailure;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParallelRpcExecutorTest {
  @Mock RpcInvokerFactory factory;

  @Mock RpcInvoker invoker1;

  @Mock RpcInvoker invoker2;

  @Mock RpcInvoker invoker3;

  @Mock VistaNameResolver resolver;

  ParallelRpcExecutor executor;

  private ConnectionDetails _connectionDetail(int n) {
    return ConnectionDetails.builder()
        .name("v" + n)
        .host("v" + n + ".com")
        .port(8000 + n)
        .divisionIen("" + n)
        .build();
  }

  @BeforeEach
  void _init() {
    executor = new ParallelRpcExecutor(factory, resolver);
  }

  private RpcPrincipal _principal(int n) {
    return RpcPrincipal.applicationProxyUserBuilder()
        .applicationProxyUser("APP PROXY " + n)
        .accessCode("ac" + n)
        .verifyCode("vc" + n)
        .build();
  }

  private RpcRequest _request() {
    return RpcRequest.builder()
        .principal(RpcPrincipal.builder().accessCode("a").verifyCode("b").build())
        .target(RpcVistaTargets.builder().forPatient("123").build())
        .rpc(RpcDetails.builder().name("WHATEVER").build())
        .build();
  }

  private RpcInvocationResult _result(int n, String... error) {
    return RpcInvocationResult.builder()
        .vista("v" + n)
        .response("" + n)
        .error(Optional.ofNullable(error.length == 0 ? null : error[0]))
        .build();
  }

  @Test
  void contextOverridesAreUsedToMakeDifferentRpcRequests() {
    ConnectionDetails c1 = _connectionDetail(1);
    ConnectionDetails c2 = _connectionDetail(2);
    ConnectionDetails c3 = _connectionDetail(3);
    var r = _request();
    RpcPrincipal p1 = _principal(1).contextOverride("C1");
    RpcPrincipal p2 = _principal(2);
    r.siteSpecificPrincipals().put(c1.name(), p1);
    r.siteSpecificPrincipals().put(c2.name(), p2);
    when(resolver.resolve(r.target())).thenReturn(List.of(c1, c2, c3));
    when(factory.create(p1, c1)).thenReturn(invoker1);
    when(factory.create(p2, c2)).thenReturn(invoker2);
    when(factory.create(_request().principal(), c3)).thenReturn(invoker3);
    RpcInvocationResult r1 = _result(1);
    RpcInvocationResult r2 = _result(2);
    RpcInvocationResult r3 = _result(3);
    when(invoker1.invoke(r.rpc().context("C1"))).thenReturn(r1);
    when(invoker2.invoke(r.rpc())).thenReturn(r2);
    when(invoker3.invoke(r.rpc())).thenReturn(r3);
    assertThat(executor.execute(r))
        .isEqualTo(RpcResponse.builder().status(Status.OK).results(List.of(r1, r2, r3)).build());
  }

  @Test
  void emptyErrorResponseIsReturnedIfNoNamesAreResolved() {
    var r = _request();
    when(resolver.resolve(r.target())).thenReturn(List.of());
    assertThat(executor.execute(r))
        .isEqualTo(RpcResponse.builder().status(Status.NO_VISTAS_RESOLVED).build());
  }

  @Test
  void exceptionFromInvokersAreHandledAndProduceErrorResponseEntries() {
    var r = _request();
    ConnectionDetails c1 = _connectionDetail(1);
    ConnectionDetails c2 = _connectionDetail(2);
    ConnectionDetails c3 = _connectionDetail(3);
    when(resolver.resolve(r.target())).thenReturn(List.of(c1, c2, c3));
    when(factory.create(_request().principal(), c1)).thenReturn(invoker1);
    when(factory.create(_request().principal(), c2)).thenReturn(invoker2);
    when(factory.create(_request().principal(), c3)).thenReturn(invoker3);
    RpcInvocationResult r1 = _result(1);
    RpcInvocationResult r2 = _result(2, "Failed to get result: RuntimeException: FUGAZI");
    RpcInvocationResult r3 = _result(3);
    r2.response(null);
    when(invoker2.vista()).thenReturn("v2");
    when(invoker1.invoke(r.rpc())).thenReturn(r1);
    when(invoker2.invoke(r.rpc())).thenThrow(new RuntimeException("FUGAZI"));
    when(invoker3.invoke(r.rpc())).thenReturn(r3);
    assertThat(executor.execute(r))
        .isEqualTo(
            RpcResponse.builder().status(Status.FAILED).results(List.of(r1, r2, r3)).build());
  }

  @Test
  void individualResponseErrorsArePropagated() {
    var r = _request();
    ConnectionDetails c1 = _connectionDetail(1);
    ConnectionDetails c2 = _connectionDetail(2);
    ConnectionDetails c3 = _connectionDetail(3);
    when(resolver.resolve(r.target())).thenReturn(List.of(c1, c2, c3));
    when(factory.create(_request().principal(), c1)).thenReturn(invoker1);
    when(factory.create(_request().principal(), c2)).thenReturn(invoker2);
    when(factory.create(_request().principal(), c3)).thenReturn(invoker3);
    RpcInvocationResult r1 = _result(1);
    RpcInvocationResult r2 = _result(2, "boom");
    RpcInvocationResult r3 = _result(3);
    when(invoker1.invoke(r.rpc())).thenReturn(r1);
    when(invoker2.invoke(r.rpc())).thenReturn(r2);
    when(invoker3.invoke(r.rpc())).thenReturn(r3);
    assertThat(executor.execute(r))
        .isEqualTo(
            RpcResponse.builder().status(Status.FAILED).results(List.of(r1, r2, r3)).build());
  }

  @Test
  void responsesAreCollectedAndReturned() {
    var r = _request();
    ConnectionDetails c1 = _connectionDetail(1);
    ConnectionDetails c2 = _connectionDetail(2);
    ConnectionDetails c3 = _connectionDetail(3);
    when(resolver.resolve(r.target())).thenReturn(List.of(c1, c2, c3));
    when(factory.create(_request().principal(), c1)).thenReturn(invoker1);
    when(factory.create(_request().principal(), c2)).thenReturn(invoker2);
    when(factory.create(_request().principal(), c3)).thenReturn(invoker3);
    RpcInvocationResult r1 = _result(1);
    RpcInvocationResult r2 = _result(2);
    RpcInvocationResult r3 = _result(3);
    when(invoker1.invoke(r.rpc())).thenReturn(r1);
    when(invoker2.invoke(r.rpc())).thenReturn(r2);
    when(invoker3.invoke(r.rpc())).thenReturn(r3);
    assertThat(executor.execute(r))
        .isEqualTo(RpcResponse.builder().status(Status.OK).results(List.of(r1, r2, r3)).build());
  }

  @Test
  void siteSpecificPrincipalIsUsedWhenAvailable() {
    ConnectionDetails c1 = _connectionDetail(1);
    ConnectionDetails c2 = _connectionDetail(2);
    ConnectionDetails c3 = _connectionDetail(3);
    var r = _request();
    r.siteSpecificPrincipals().put(c1.name(), _principal(1));
    r.siteSpecificPrincipals().put(c2.name(), _principal(2));
    // site 3 will use default
    when(resolver.resolve(r.target())).thenReturn(List.of(c1, c2, c3));
    when(factory.create(_principal(1), c1)).thenReturn(invoker1);
    when(factory.create(_principal(2), c2)).thenReturn(invoker2);
    when(factory.create(r.principal(), c3)).thenReturn(invoker3);
    RpcInvocationResult r1 = _result(1);
    RpcInvocationResult r2 = _result(2);
    RpcInvocationResult r3 = _result(3);
    when(invoker1.invoke(r.rpc())).thenReturn(r1);
    when(invoker2.invoke(r.rpc())).thenReturn(r2);
    when(invoker3.invoke(r.rpc())).thenReturn(r3);
    assertThat(executor.execute(r))
        .isEqualTo(RpcResponse.builder().status(Status.OK).results(List.of(r1, r2, r3)).build());
  }

  @Test
  void unrecoverableExceptionFromInvokersArePropagated() {
    var r = _request();
    ConnectionDetails c1 = _connectionDetail(1);
    ConnectionDetails c2 = _connectionDetail(2);
    ConnectionDetails c3 = _connectionDetail(3);
    /*
     * Depending on your environment, these tests may execute faster or slower. It's possible in
     * slower environments that the less parallelization means one or more of these `whens` are not
     * executed by the time the exception is thrown.
     */
    lenient().when(resolver.resolve(r.target())).thenReturn(List.of(c1, c2, c3));
    lenient().when(factory.create(_request().principal(), c1)).thenReturn(invoker1);
    lenient().when(factory.create(_request().principal(), c2)).thenReturn(invoker2);
    lenient().when(factory.create(_request().principal(), c3)).thenReturn(invoker3);
    RpcInvocationResult r1 = _result(1);
    RpcInvocationResult r2 = _result(2, "Failed to get result: RuntimeException: FUGAZI");
    RpcInvocationResult r3 = _result(3);
    r2.response(null);
    lenient().when(invoker2.vista()).thenReturn("v2");
    lenient().when(invoker1.invoke(r.rpc())).thenReturn(r1);
    lenient().when(invoker2.invoke(r.rpc())).thenThrow(new LoginFailure("FUGAZI"));
    lenient().when(invoker3.invoke(r.rpc())).thenReturn(r3);
    assertThatExceptionOfType(LoginFailure.class).isThrownBy(() -> executor.execute(r));
  }
}
