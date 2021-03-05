package gov.va.api.lighthouse.charon.models;

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
    public InvalidVistaResponse(String message) {
      super(message);
    }

    public InvalidVistaResponse(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
