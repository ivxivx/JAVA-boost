package poc.boost.temporalio;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.util.JsonFormat;

import poc.boost.config.ConfigService;
import poc.boost.util.ProtobufUtils;

@Service
@Slf4j
public class TemporalService {
    @Autowired
    private ConfigService configService;

    public List<Map<String, Object>> getWorkflowExecutions(ListWorkflowExecutionsRequest request) {
        var builder = io.temporal.api.workflowservice.v1.ListWorkflowExecutionsRequest.newBuilder()
            .setNamespace(temporalClient().getOptions().getNamespace());

        if (request.getQuery() != null) {
            builder.setQuery(request.getQuery());
        }

        var listWorkflowExecutionsRequest = builder.build();

        var listWorkflowExecutionsResponse = temporalClient().getClient()
            .getWorkflowServiceStubs()
            .blockingStub()
            .listWorkflowExecutions(listWorkflowExecutionsRequest);

        var result = listWorkflowExecutionsResponse.getExecutionsList().stream().map(e -> {
            e.getAllFields();
            return ProtobufUtils.convertToMap(e);
        })
        .collect(Collectors.toList());

        return result;
    }

    private TemporalClient temporalClient() {
        return configService.getTemporalClient();
    }
}
