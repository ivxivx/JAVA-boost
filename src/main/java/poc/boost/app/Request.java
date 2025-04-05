package poc.boost.app;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import poc.boost.config.Namespace;

@AllArgsConstructor
@Data
@NoArgsConstructor
@SuperBuilder
@ToString
public abstract class Request {
    @Builder.Default
    private Namespace namespace = Namespace.DEFAULT;
}
