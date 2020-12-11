package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcVistaTargets;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.api.lighthouse.vistalink.service.controller.VistaLinkExceptions.VistaLinkException;
import java.util.List;

public interface VistaNameResolver {
  List<ConnectionDetails> resolve(RpcVistaTargets rpcVistaTargets);

  class NameResolutionException extends VistaLinkException {
    public NameResolutionException(String message) {
      super(message);
    }

    public NameResolutionException(Exception cause) {
      super(cause);
    }
  }
}
