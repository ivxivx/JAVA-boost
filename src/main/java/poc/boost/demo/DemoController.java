package poc.boost.demo;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import poc.boost.app.BaseController;
import poc.boost.app.ControllerMetadata;
import poc.boost.app.ControllerType;
import poc.boost.app.ListResponse;

// demo http, jdbc, log, redis, kafka, ssh
@ControllerMetadata(type = ControllerType.Demo)
@RequestMapping("/demo")
@RestController
@Slf4j
public class DemoController implements BaseController {
  @Autowired
  private DemoService demoService;

  @RequestMapping(value = "/orders", method = {RequestMethod.POST})
  public ListResponse createOrder(@RequestBody CreateOrderRequest request) throws IOException {
    log.debug("request {}", request);

    return ListResponse.builder().data(Collections.singletonList(demoService.createOrder(request))).build();
  }
}
