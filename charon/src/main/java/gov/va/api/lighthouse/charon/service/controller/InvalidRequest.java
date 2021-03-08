package gov.va.api.lighthouse.charon.service.controller;

public class InvalidRequest extends IllegalArgumentException {
  public InvalidRequest(String message) {
    super(message);
  }
}
