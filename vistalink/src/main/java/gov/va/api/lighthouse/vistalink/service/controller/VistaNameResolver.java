package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.lighthouse.vistalink.service.api.RpcVistaTargets;
import gov.va.api.lighthouse.vistalink.service.config.ConnectionDetails;
import gov.va.api.lighthouse.vistalink.service.controller.VistaLinkExceptions.VistaLinkException;
import java.util.List;
import lombok.Getter;

public interface VistaNameResolver {
  List<ConnectionDetails> resolve(RpcVistaTargets rpcVistaTargets);

  @Getter
  class NameResolutionException extends VistaLinkException {

    private final String publicErrorCode;

    /** Create a new instance for the given error code and message. */
    public NameResolutionException(Enum<?> publicErrorCode, String message) {
      super(message);
      this.publicErrorCode = publicErrorCode.toString();
    }

    /** Create a new instance for the given error code and root cause. */
    public NameResolutionException(Enum<?> publicErrorCode, Exception cause) {
      super(cause);
      this.publicErrorCode = publicErrorCode.toString();
    }
  }
}
