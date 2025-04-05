package poc.boost.config;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import javax.annotation.PostConstruct;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;

import poc.boost.app.BoostRuntimeException;
import poc.boost.database.JdbcConfig;
import poc.boost.redis.RedisConfig;

@Service
@Slf4j
public class ConfigService {
    private Map<Namespace, Config> configMap = new ConcurrentHashMap<>();

    private final ThreadLocal<Config> configs = new ThreadLocal<Config>() {
        @Override
        protected Config initialValue() {
            return configMap.get(Namespace.DEFAULT);
        }
    };

    @PostConstruct
    public void postConstruct() {
        TomlMapper mapper = new TomlMapper();

        ConfigFile configFile;
        try {
            val file = ResourceUtils.getFile("classpath:application.toml");
    
            configFile = mapper.readerFor(ConfigFile.class).readValue(file);
        } catch (IOException e) {
            throw new BoostRuntimeException("failed to read config file", e);
        }

        init(configMap, configFile.getJdbc().entrySet(), (config, properties) -> {
            val jdbcTemplate = JdbcConfig.createConfig(properties);

            config.setJdbcTemplate(jdbcTemplate);
        });

        init(configMap, configFile.getRedis().entrySet(), (config, properties) -> {
            val stringRedisTemplate = RedisConfig.createConfig(properties);

            config.setStringRedisTemplate(stringRedisTemplate);
        });
    }

    private void init(
        Map<Namespace, Config> configMap, 
        Set<Map.Entry<Namespace, Property>> entrySet,
        BiConsumer<Config, Properties> callback
    ) {
        for (val entry : entrySet) {
            val namespace = entry.getKey();
            var config = configMap.get(namespace);

            if (config == null) {
                config = Config.builder()
                    .namespace(namespace)
                    .build();

                configMap.put(namespace, config);
            }

            val property = entry.getValue();
            val properties = property.convertToProperties();

            callback.accept(config, properties);
        }
    }

    public void setCurrentConfig(Namespace namespace) {
        Config config = configMap.get(namespace);

        configs.set(config);
    }

    public JdbcTemplate getJdbcTemplate() {
        return configs.get().getJdbcTemplate();
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return configs.get().getStringRedisTemplate();
    }
}
