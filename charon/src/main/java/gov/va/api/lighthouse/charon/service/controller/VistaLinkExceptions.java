package gov.va.api.lighthouse.charon.service.controller;

import java.io.Serial;
import lombok.Getter;
import lombok.experimental.UtilityClass;

/** Container class for all Vistalink Exceptions. */
@UtilityClass
public class VistaLinkExceptions {

  /** Name resolution. */
  @Getter
  public static class NameResolutionException extends VistaLinkException {

    @Serial private static final long serialVersionUID = 8280519539296791178L;

    private final String publicErrorCode;

    /** Create a new instance for the given error code and message. */
    public NameResolutionException(Enum<?> publicErrorCode, String message, Exception cause) {
      super(message, cause);
      this.publicErrorCode = publicErrorCode == null ? "" : publicErrorCode.toString();
    }
  }

  /** Unknown Patient. */
  public static class UnknownPatient extends NameResolutionException {

    @Serial private static final long serialVersionUID = 4542527678706542746L;

    public UnknownPatient(Enum<?> publicErrorCode, String message) {
      super(publicErrorCode, message, null);
    }
  }

  /** Unknown vista. */
  public static final class UnknownVista extends VistaLinkException {

    @Serial private static final long serialVersionUID = 8519965629992550013L;

    public UnknownVista(String names) {
      super(names);
    }
  }

  /** Base Vistalink Exception. */
  public static class VistaLinkException extends RuntimeException {

    @Serial private static final long serialVersionUID = 3791939028343499833L;

    public VistaLinkException(String message) {
      super(message);
    }

    public VistaLinkException(String message, Exception cause) {
      super(message, cause);
    }
  }
}
