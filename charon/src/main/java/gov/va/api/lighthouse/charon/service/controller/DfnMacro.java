package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class DfnMacro implements Macro {

  @SneakyThrows
  @Override
  public String evaluate(MacroExecutionContext ctx, ConnectionDetails details, String value) {
    var vistalinkRequest = RpcRequestFactory.getRpcRequest();
    vistalinkRequest.setRpcContext("VAFCTF RPC CALLS");
    vistalinkRequest.setUseProprietaryMessageFormat(true);
    vistalinkRequest.setRpcName("VAFCTFU CONVERT ICN TO DFN");
    vistalinkRequest.getParams().setParam(1, "string", value);
    String result = ctx.invoke(vistalinkRequest).getResults();
    if ("-1^ICN NOT IN DATABASE".equals(result)) {
      throw new IcnNotFound();
    }
    return result;
  }

  @Override
  public String name() {
    return "dfn";
  }

  static class IcnNotFound extends RuntimeException {}
}
