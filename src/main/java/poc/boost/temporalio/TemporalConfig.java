package poc.boost.temporalio;

import java.util.Properties;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import lombok.val;

public class TemporalConfig {
    public static TemporalClient createConfig(Properties properties) {
        val namespace = properties.getProperty("namespace");

        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();

        WorkflowClientOptions clientOptions = WorkflowClientOptions
            .newBuilder()
            .setNamespace(namespace)
            .build();

        WorkflowClient client = WorkflowClient.newInstance(service, clientOptions);

        return new TemporalClient(client, clientOptions);
    }
}
