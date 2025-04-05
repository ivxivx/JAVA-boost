package poc.boost.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
public class RedisOpsForHashSetRequest extends RedisRequest {
    private String key;
    private String hashKey;
    private Object hashValue;
    private boolean ifAbsent;
}
