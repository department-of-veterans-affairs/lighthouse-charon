package gov.va.api.lighthouse.vistalink.service.controller;

import gov.va.api.health.autoconfig.logging.Redact;
import gov.va.api.lighthouse.vistalink.service.api.RpcRequest;
import gov.va.api.lighthouse.vistalink.service.api.RpcResponse;
import gov.va.api.lighthouse.vistalink.service.config.VistalinkProperties;
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

@Validated
@RestController()
@RequestMapping(
    path = "/rpc",
    produces = {"application/json"})
@AllArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class RpcController {

  private final RpcExecutor rpcExecutor;
  private final VistalinkProperties vistalinkProperties;

  @GetMapping("/connections")
  public VistalinkProperties connections() {
    return vistalinkProperties;
  }

  /** Process the RPC request. */
  @PostMapping(consumes = {"application/json"})
  public RpcResponse invoke(@Redact @RequestBody @Valid RpcRequest request) {
    log.info("Target {}", request.target());
    log.info("RPC {}", request.rpc());
    return rpcExecutor.execute(request);
  }
}
