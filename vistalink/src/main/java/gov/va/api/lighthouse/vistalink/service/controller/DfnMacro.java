package gov.va.api.lighthouse.vistalink.service.controller;

public class DfnMacro implements Macro {

  @Override
  public String evaluate(MacroExecutionContext ctx, String value) {
    //        RpcRequest body =
    //                RpcRequest.builder()
    //                        .rpc(RpcDetails.builder().context("VAFCTF RPC CALLS")
    //                                .name("VAFCTFU CONVERT ICN TO DFN")
    //                                .parameters(Collections.singletonList(RpcDetails.Parameter
    //                                        .builder().string(value).build())).build())
    //                        .principal(RpcPrincipal.builder()
    //                                .accessCode(System.getProperty("vista.access-code",
    // "not-set"))
    //                                .verifyCode(System.getProperty("vista.verify-code",
    // "not-set"))
    //                                .build())
    //
    // .target(RpcVistaTargets.builder().forPatient("ignored-for-now").build())
    //                        .build();
    //        return ctx.invoke(body).results().get(0).response();
    return "todoDfnMacro";
  }

  @Override
  public String name() {
    return "dfn";
  }
}
