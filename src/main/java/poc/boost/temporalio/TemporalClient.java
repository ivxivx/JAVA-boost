package poc.boost.temporalio;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import poc.boost.app.Request;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
public class TemporalClient extends Request {
    private WorkflowClient client;

    private WorkflowClientOptions options;
}
