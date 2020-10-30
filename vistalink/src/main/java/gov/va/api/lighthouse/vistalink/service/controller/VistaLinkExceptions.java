package gov.va.api.lighthouse.vistalink.service.controller;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class VistaLinkExceptions {

  static class VistaLinkException extends Exception {
    public VistaLinkException(String s) {
      super(s);
    }
  }

  public static final class VistaLoginException extends VistaLinkException {
    public VistaLoginException(String s) {
      super(s);
    }
  }
}
