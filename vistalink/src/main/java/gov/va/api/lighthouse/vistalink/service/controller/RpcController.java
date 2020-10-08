package gov.va.api.lighthouse.vistalink.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RpcController {

  ParallelRpcExecutor parallelRpcExecutor;

  RpcController(@Autowired ParallelRpcExecutor parallelRpcExecutor) {
    this.parallelRpcExecutor = parallelRpcExecutor;
  }
}
