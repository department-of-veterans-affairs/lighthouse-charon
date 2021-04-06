package gov.va.api.lighthouse.charon.tests;

import static gov.va.api.lighthouse.charon.tests.TestOptions.assumeEnabled;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.config.VistalinkProperties;
import gov.va.api.lighthouse.charon.service.controller.AllVistaNameResolver;
import gov.va.api.lighthouse.charon.service.controller.DfnMacro;
import gov.va.api.lighthouse.charon.service.controller.LocalDateMacro;
import gov.va.api.lighthouse.charon.service.controller.MacroProcessorFactory;
import gov.va.api.lighthouse.charon.service.controller.NameResolution;
import gov.va.api.lighthouse.charon.service.controller.ParallelRpcExecutor;
import gov.va.api.lighthouse.charon.service.controller.VistalinkRpcInvokerFactory;
import java.io.File;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class FileRpcRequestTest {

  private final ObjectMapper mapper = JacksonConfig.createMapper();

  private List<ConnectionDetails> connectionDetails(RpcVistaTargets target) {
    return NameResolution.builder()
        .properties(VistalinkProperties.builder().build())
        .additionalCandidates(r -> List.of())
        .build()
        .resolve(target);
  }

  @Test
  @SneakyThrows
  void invokeRequest() {
    assumeEnabled("test.file-rpc-request");
    var invokerFactory =
        new VistalinkRpcInvokerFactory(
            new MacroProcessorFactory(List.of(new DfnMacro(), new LocalDateMacro())));
    var request = loadRequest();
    ParallelRpcExecutor executor =
        new ParallelRpcExecutor(
            invokerFactory, new AllVistaNameResolver(VistalinkProperties.builder().build()));
    RpcResponse response = executor.execute(request);
    log.info(
        "RESPONSE START\n{}\nRESPONSE END",
        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
  }

  @SneakyThrows
  RpcRequest loadRequest() {
    String fileName = TestOptions.valueOf("test.file-rpc-request.file", "/request.json");
    return mapper.readValue(new File(fileName), RpcRequest.class);
  }
}
