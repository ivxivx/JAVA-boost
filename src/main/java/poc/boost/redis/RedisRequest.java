package poc.boost.redis;

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
public class RedisRequest extends Request {
    @Builder.Default
    private boolean isJson = true;

    /**
     * UPPER: convert key to uppercase
     * LOWER: convert key to lowercase
     * null: do not convert
     */
    private String keyCase;
}
