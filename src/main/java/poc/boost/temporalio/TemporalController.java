package poc.boost.temporalio;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import poc.boost.app.BaseController;
import poc.boost.app.ControllerMetadata;
import poc.boost.app.ControllerType;
import poc.boost.app.ListResponse;

@ControllerMetadata(type = ControllerType.TemporalIO)
@RequestMapping("/temporalio")
@RestController
@Slf4j
public class TemporalController implements BaseController {
    @Autowired
    private TemporalService temporalService;

    @RequestMapping(value = "/workflow-executions/read", method = {RequestMethod.POST})
    public ListResponse getAllWorkflowExecutions(@RequestBody ListWorkflowExecutionsRequest request) {
        List<Map<String, Object>> result = temporalService.getWorkflowExecutions(request);

        return ListResponse.builder().data(result).build();
    }
}