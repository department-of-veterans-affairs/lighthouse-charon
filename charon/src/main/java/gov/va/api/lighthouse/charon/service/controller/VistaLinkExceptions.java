package gov.va.api.lighthouse.charon.service.controller;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VistaLinkExceptions {

  @Getter
  public static class NameResolutionException extends VistaLinkException {

    private final String publicErrorCode;

    /** Create a new instance for the given error code and message. */
    public NameResolutionException(Enum<?> publicErrorCode, String message, Exception cause) {
      super(message, cause);
      this.publicErrorCode = publicErrorCode == null ? "" : publicErrorCode.toString();
    }
  }

  public static class UnknownPatient extends NameResolutionException {
    public UnknownPatient(Enum<?> publicErrorCode, String message) {
      super(publicErrorCode, message, null);
    }
  }

  public static final class UnknownVista extends VistaLinkException {
    public UnknownVista(String names) {
      super(names);
    }
  }

  public static class VistaLinkException extends RuntimeException {
    public VistaLinkException(String message) {
      super(message);
    }

    public VistaLinkException(String message, Exception cause) {
      super(message, cause);
    }
  }
}
