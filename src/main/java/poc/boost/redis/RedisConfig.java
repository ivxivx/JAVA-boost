package poc.boost.redis;

import lombok.val;

import java.util.Properties;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisConfig {
    public static StringRedisTemplate createConfig(Properties properties) {
        val servers = properties.getProperty("servers");

        val nodes = servers.split(",");

        if (nodes.length == 1) {
            val parts = servers.split(":");
    
            val redisConfig = new RedisStandaloneConfiguration(parts[0], Integer.parseInt(parts[1]));
            val factory = new LettuceConnectionFactory(redisConfig);
            factory.afterPropertiesSet();

            return new StringRedisTemplate(factory);
        } else {
            // cluster mode
            throw new IllegalArgumentException("multiple redis servers are not supported");
        }
    }
}
