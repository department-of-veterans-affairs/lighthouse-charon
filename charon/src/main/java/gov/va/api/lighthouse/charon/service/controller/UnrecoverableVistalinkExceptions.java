package gov.va.api.lighthouse.charon.service.controller;

import java.io.Serial;
import lombok.experimental.UtilityClass;

@UtilityClass
final class UnrecoverableVistalinkExceptions {

  static class BadRpcContext extends UnrecoverableVistalinkException {

    @Serial private static final long serialVersionUID = 6167518157729577717L;

    BadRpcContext(String rpcContext, Throwable cause) {
      super(rpcContext, cause);
    }
  }

  public static class LoginFailure extends UnrecoverableVistalinkException {

    @Serial private static final long serialVersionUID = 4860029675281471237L;

    public LoginFailure(String message) {
      super(message, null);
    }
  }

  static class UnrecoverableVistalinkException extends RuntimeException {

    @Serial private static final long serialVersionUID = -6893741398933405923L;

    UnrecoverableVistalinkException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
