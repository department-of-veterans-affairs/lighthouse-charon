package gov.va.api.lighthouse.vistalink.service.controller;

import lombok.experimental.UtilityClass;

@UtilityClass
public class VistaLinkExceptions {
  public static final class VistaLoginFailed extends VistaLinkException {
    public VistaLoginFailed(String s) {
      super(s);
    }
  }

  public static class VistaLinkException extends RuntimeException {
    public VistaLinkException(String s) {
      super(s);
    }
  }
}
