package gov.va.api.lighthouse.vistalink.models;

import lombok.experimental.UtilityClass;

@UtilityClass
public class VistalinkModelExceptions {

  static class VistaModelException extends RuntimeException {
    VistaModelException(String message, Throwable cause) {
      super(message, cause);
    }

    VistaModelException(String message) {
      super(message);
    }
  }

  public static final class InvalidVistaResponse extends VistaModelException {
    public InvalidVistaResponse() {
      super(String.format("VistA response is invalid"));
    }

    public InvalidVistaResponse(Throwable cause) {
      super(String.format("VistA response is invalid"), cause);
    }
  }
}
