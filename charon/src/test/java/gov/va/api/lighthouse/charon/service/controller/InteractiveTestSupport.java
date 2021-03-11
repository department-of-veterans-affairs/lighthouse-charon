package gov.va.api.lighthouse.charon.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.charon.api.RpcDetails;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.med.exception.FoundationsException;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import java.util.List;

class InteractiveTestSupport {
  static MacroProcessorFactory emptyMacroProcessorFactory() {
    return new MacroProcessorFactory(List.of());
  }

  static ConnectionDetails localTampaConnectionDetails() {
    return ConnectionDetails.builder()
        .name("673")
        .host("localhost")
        .port(18673)
        .divisionIen("673")
        .timezone("America/New_York")
        .build();
  }

  static MacroExecutionContext nullMacroExecutionContext() {
    return request -> null;
  }

  static String requirePropertyValue(String name) {
    String value = System.getProperty(name);
    assertThat(value).withFailMessage("System property %s not defined", name).isNotBlank();
    return value;
  }

  static RpcRequest toRpcRequest(RpcDetails rpcDetails) throws FoundationsException {
    var vistalinkRequest = RpcRequestFactory.getRpcRequest();
    vistalinkRequest.setRpcContext(rpcDetails.context());
    vistalinkRequest.setUseProprietaryMessageFormat(true);
    vistalinkRequest.setRpcName(rpcDetails.name());
    if (rpcDetails.version().isPresent()) {
      vistalinkRequest.setRpcVersion(rpcDetails.version().get());
    }
    MacroProcessor macroProcessor =
        emptyMacroProcessorFactory().create(nullMacroExecutionContext());
    for (int i = 0; i < rpcDetails.parameters().size(); i++) {
      var parameter = rpcDetails.parameters().get(i);
      var value = macroProcessor.evaluate(parameter);
      vistalinkRequest.getParams().setParam(i + 1, parameter.type(), value);
    }
    return vistalinkRequest;
  }
}
