package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.med.exception.FoundationsException;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DfnMacro implements Macro {

  @Override
  public String evaluate(MacroExecutionContext ctx, String value) {
    try {
      var vistalinkRequest = RpcRequestFactory.getRpcRequest();
      vistalinkRequest.setRpcContext("VAFCTF RPC CALLS");
      vistalinkRequest.setUseProprietaryMessageFormat(true);
      vistalinkRequest.setRpcName("VAFCTFU CONVERT ICN TO DFN");
      vistalinkRequest.getParams().setParam(0, "string", value);
      String result = ctx.invoke(vistalinkRequest).getResults();
      if (result.equals("-1^ICN NOT IN DATABASE")) {
        throw new InvalidRequest(result);
      }
      return result;
    } catch (FoundationsException e) {
      log.info("Exception: " + e.getMessage());
    }
    return value;
  }

  @Override
  public String name() {
    return "dfn";
  }
}
