package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.health.autoconfig.logging.Redact;
import gov.va.api.lighthouse.charon.api.RpcRequest;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import gov.va.api.lighthouse.charon.service.config.EncyptedLoggingConfig.EncryptedLogging;
import gov.va.api.lighthouse.charon.service.config.VistalinkProperties;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller for making RPC requests. */
@Validated
@RestController
@RequestMapping(
    path = "/rpc",
    produces = {"application/json"})
@Slf4j
@AllArgsConstructor(onConstructor_ = @Autowired)
public class RpcController {

  private final RpcExecutor rpcExecutor;
  private final VistalinkProperties vistalinkProperties;
  private final EncryptedLogging encryptedLogging;

  @GetMapping("/connections")
  public VistalinkProperties connections() {
    return vistalinkProperties;
  }

  /** Process the RPC request. */
  @PostMapping(consumes = {"application/json"})
  public RpcResponse invoke(@Redact @RequestBody @Valid RpcRequest request) {
    log.info("Request: {}", encryptedLogging.encrypt(request.toString()));
    return rpcExecutor.execute(request);
  }
}
