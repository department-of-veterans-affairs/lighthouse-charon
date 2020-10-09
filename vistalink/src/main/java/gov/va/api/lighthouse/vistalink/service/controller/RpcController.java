package gov.va.api.lighthouse.vistalink.service.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

// TODO: Request Mappings

@Slf4j
@RestController
@AllArgsConstructor(onConstructor_ = @Autowired)
public class RpcController {

  private final RpcExecutor rpcExecutor;

  public void placeholderLogging() {
    log.info("{}", rpcExecutor);
  }
}
