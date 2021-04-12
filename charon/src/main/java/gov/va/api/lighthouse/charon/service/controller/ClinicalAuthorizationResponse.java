package gov.va.api.lighthouse.charon.service.controller;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ClinicalAuthorizationResponse {
  String status;
  String rawAuthorizationResponse;
}
