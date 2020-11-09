package gov.va.api.lighthouse.vistalink.service.controller;

import lombok.experimental.UtilityClass;

@UtilityClass
final class UnrecoverableVistalinkExceptions {

  static class UnrecoverableVistalinkException extends RuntimeException {
    UnrecoverableVistalinkException(String s) {
      super(s);
    }
  }

  static class BadRpcContext extends UnrecoverableVistalinkException {
    BadRpcContext(String s) {
      super(s);
    }
  }
}
