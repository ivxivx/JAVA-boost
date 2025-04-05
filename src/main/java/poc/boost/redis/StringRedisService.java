package poc.boost.redis;

import lombok.extern.slf4j.Slf4j;
import poc.boost.config.ConfigService;
import poc.boost.util.JsonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
public class StringRedisService {
    @Autowired
    private ConfigService configService;

    public List<Object> opsForValueGet(RedisOpsForValueGetRequest request) {
        String key = normalizeKey(request.getKeyCase(), request.getKey());
        String value = stringRedisTemplate().opsForValue().get(key);

        Object parsed = Optional.ofNullable(value).map(e -> {
            if (request.isJson()) {
                return JsonUtils.readAsMap(e);
            } else {
                return e;
            }
        }).orElse(null);

        List<Object> result = parsed == null ? Collections.emptyList() : Collections.singletonList(parsed);

        return result;
    }

    public List<Object> opsForValueGetMultiple(RedisOpsForValueGetMultipleRequest request) {
        List<Object> result = request.getKeys().stream()
            .map(key -> {
                String k = request.getPrefix() + key;
                k = normalizeKey(request.getKeyCase(), k);
                return stringRedisTemplate().opsForValue().get(k);
            })
            .filter(Objects::nonNull)
            .map(e -> {
                if (request.isJson()) {
                    return JsonUtils.readAsMap(e);
                } else {
                    return e;
                }
            })
            .collect(Collectors.toList());

        return result;
    }

    public Object opsForHashGet(RedisOpsForHashGetRequest request) {
        String key = normalizeKey(request.getKeyCase(), request.getKey());
        HashOperations<String, String, String> ops = stringRedisTemplate().opsForHash();

        String hashValue = ops.get(key, request.getHashKey());

        if (request.isJson()) {
            return JsonUtils.readAsMap(hashValue);
        } else {
            return hashValue;
        }
    }

    public Boolean opsForHashSet(RedisOpsForHashSetRequest request) {
        String key = normalizeKey(request.getKeyCase(), request.getKey());
        HashOperations<String, String, String> ops = stringRedisTemplate().opsForHash();

        var value = JsonUtils.writeAsString(request.getHashValue());
        
        Boolean added = false;

        if (request.isIfAbsent()) {
            added = ops.putIfAbsent(key, request.getHashKey(), value);
        } else {
            ops.put(key, request.getHashKey(), value);

            added = true;
        }
        
        return added;
    }

    public List<Object> scanGet(RedisScanGetRequest request) {
        List<Object> result = stringRedisTemplate().execute(new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                String pattern = normalizeKey(request.getKeyCase(), request.getPattern());
                //count(max) is just a suggestion, all keys matching the pattern may be returned even max is specified
                ScanOptions options = ScanOptions.scanOptions().match(pattern).count(request.getMax()).build();

                List<Object> values = new ArrayList<>(10);
                Cursor<byte[]> cursor = connection.scan(options);

                while (cursor.hasNext()) {
                    String value = new String(cursor.next());
                    log.info("Read value from redis {}", value);
                    values.add(value);
                }

                return values;
            }
        });

        return result;
    }

    public Long delete(@RequestBody RedisDeleteRequest request) {
        RedisScanGetRequest scanGetRedisRequest = new RedisScanGetRequest();
        scanGetRedisRequest.setMax(request.getMax());
        scanGetRedisRequest.setPattern(request.getPattern());
        scanGetRedisRequest.setJson(false);

        log.info("Delete all values from redis {}", scanGetRedisRequest);

        List<Object> response = scanGet(scanGetRedisRequest);

        List<String> keys = response.stream().map(data -> (String)data).collect(Collectors.toList());

        Long num = stringRedisTemplate().delete(keys);

        log.info("{} keys are deleted", num);

        return num;
    }

    private String normalizeKey(String keyCase, String key) {
        if ("UPPER".equalsIgnoreCase(keyCase)) {
            return key.toUpperCase(Locale.ROOT);
        } else if ("LOWER".equalsIgnoreCase(keyCase)) {
            return key.toLowerCase(Locale.ROOT);
        } else {
            return key;
        }
    }

    private StringRedisTemplate stringRedisTemplate() {
        return configService.getStringRedisTemplate();
    }
}
