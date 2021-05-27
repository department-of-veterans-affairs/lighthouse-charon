package gov.va.api.lighthouse.charon.models;

import lombok.experimental.UtilityClass;

/** Container class for all model exceptions. */
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

  /** InvalidRpcResponse. */
  public static final class InvalidRpcResponse extends RpcModelException {
    public InvalidRpcResponse(String message) {
      super(message);
    }

    public InvalidRpcResponse(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
