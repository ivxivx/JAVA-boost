package poc.boost.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poc.boost.temporalio.TemporalClient;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Config {
    private Namespace namespace;
    
    private JdbcTemplate jdbcTemplate;
    private StringRedisTemplate stringRedisTemplate;
    private TemporalClient temporalClient;
}
