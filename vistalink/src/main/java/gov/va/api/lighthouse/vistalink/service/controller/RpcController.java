package gov.va.api.lighthouse.vistalink.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

// TODO: Request Mappings

@RestController
public class RpcController {

  RpcExecutor rpcExecutor;

  RpcController(@Autowired RpcExecutor rpcExecutor) {
    this.rpcExecutor = rpcExecutor;
  }
}
