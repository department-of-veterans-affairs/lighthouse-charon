package gov.va.api.lighthouse.vistalink.service.controller;

public class InvalidRequest extends IllegalArgumentException {
  public InvalidRequest(String message) {
    super(message);
  }
}
