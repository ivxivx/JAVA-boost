package poc.boost.temporalio;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class ListWorkflowExecutionsRequest extends Request {
    private String query;

    @Builder.Default
    private int pageSize = 100;

    @Builder.Default
    private int pageNumber = 1;
}
