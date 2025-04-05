package poc.boost.actuator;

import org.springframework.boot.actuate.autoconfigure.system.DiskSpaceHealthIndicatorProperties;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;

@Configuration
public class ActuatorConfig {
    @Bean
    public ReactiveHealthContributor redisHealthIndicator(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        return new RedisHealthIndicator(reactiveRedisConnectionFactory);
    }

    @Bean
    public DiskSpaceHealthIndicator diskSpaceHealthIndicator(DiskSpaceHealthIndicatorProperties properties) {
        return new DiskSpaceHealthIndicator(properties.getPath(), properties.getThreshold());
    }
}
