package gov.va.api.lighthouse.vistalink.service.controller;

import lombok.experimental.UtilityClass;

@UtilityClass
public class VistaLinkExceptions {
  public static class VistaLinkException extends RuntimeException {
    public VistaLinkException(String s) {
      super(s);
    }
  }

  public static final class UnknownVista extends VistaLinkException {
    public UnknownVista(String s) {
      super(s);
    }
  }
}
