package poc.boost.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class RedisDeleteRequest extends RedisRequest {
    //max number of keys to be deleted
    @Builder.Default
    private int max = 10000;
    @Builder.Default
    private String pattern = "*";
}
