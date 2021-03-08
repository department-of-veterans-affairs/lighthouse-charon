package gov.va.api.lighthouse.charon.models;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RpcModelExceptions {

  static class RpcModelException extends RuntimeException {
    RpcModelException(String message, Throwable cause) {
      super(message, cause);
    }

    RpcModelException(String message) {
      super(message);
    }
  }

  public static final class InvalidRpcResponse extends RpcModelException {
    public InvalidRpcResponse(String message) {
      super(message);
    }

    public InvalidRpcResponse(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
