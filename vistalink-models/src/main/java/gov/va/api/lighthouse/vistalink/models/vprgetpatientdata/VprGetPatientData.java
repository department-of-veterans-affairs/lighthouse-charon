package gov.va.api.lighthouse.vistalink.models.vprgetpatientdata;

import gov.va.api.lighthouse.vistalink.models.XmlResponseRpc;
import java.util.List;
import lombok.Data;

public class VprGetPatientData extends XmlResponseRpc {
  private static final String RPC_NAME = "VPR GET PATIENT DATA";

  private static final String RPC_CONTEXT = "VPR APPLICATION PROXY";

  VprGetPatientData deserializeResponse(String response) {
    return deserialize(response, getClass());
  }

  @Override
  public String rpcContext() {
    return RPC_CONTEXT;
  }

  @Override
  public String rpcName() {
    return RPC_NAME;
  }

  @Data
  static class Results {
    List<Vital> vitals;
  }
}
