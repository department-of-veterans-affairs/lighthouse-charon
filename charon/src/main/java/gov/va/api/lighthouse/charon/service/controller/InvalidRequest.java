package gov.va.api.lighthouse.charon.service.controller;

/** Invalid Requests. */
public class InvalidRequest extends IllegalArgumentException {
  public InvalidRequest(String message) {
    super(message);
  }
}
