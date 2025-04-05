package poc.boost.redis;

import poc.boost.app.BaseController;
import poc.boost.app.ControllerMetadata;
import poc.boost.app.ControllerType;
import poc.boost.app.ListResponse;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@ControllerMetadata(type = ControllerType.Redis)
@RequestMapping("/string-redis")
@RestController
@Slf4j
public class StringRedisController implements BaseController {
    @Autowired
    private StringRedisService stringRedisService;

    @RequestMapping(value = "/opsForValue/get", method = {RequestMethod.POST})
    public ListResponse opsForValueGet(@RequestBody RedisOpsForValueGetRequest request) {
        List<Object> result = stringRedisService.opsForValueGet(request);

        return ListResponse.builder().data(result).build();
    }

    @RequestMapping(value = "/opsForValue/getMultiple", method = {RequestMethod.POST})
    public ListResponse opsForValueGetMultiple(@RequestBody RedisOpsForValueGetMultipleRequest request) {
        List<Object> result = stringRedisService.opsForValueGetMultiple(request);

        return ListResponse.builder().data(result).build();
    }

    @RequestMapping(value = "/opsForHash/get", method = {RequestMethod.POST})
    public ListResponse opsForHashGet(@RequestBody RedisOpsForHashGetRequest request) {
        Object result = stringRedisService.opsForHashGet(request);

        return ListResponse.builder()
            .data(result == null ? Collections.emptyList() : Collections.singletonList(result))
            .build();
    }

    @RequestMapping(value = "/opsForHash/set", method = {RequestMethod.POST})
    public ListResponse opsForHashGet(@RequestBody RedisOpsForHashSetRequest request) {
        Boolean result = stringRedisService.opsForHashSet(request);

        return ListResponse.builder()
            .data(result == null ? Collections.emptyList() : Collections.singletonList(result))
            .build();
    }

    @RequestMapping(value = "/scan/get", method = {RequestMethod.POST})
    public ListResponse scanGet(@RequestBody RedisScanGetRequest request) {
        List<Object> result = stringRedisService.scanGet(request);

        return ListResponse.builder().data(result).build();
    }

    @RequestMapping(value = "/scan/delete", method = {RequestMethod.DELETE})
    public ListResponse delete(@RequestBody RedisDeleteRequest request) {
        Long num = stringRedisService.delete(request);

        return ListResponse.builder()
            .data(num == null ? Collections.emptyList() : Collections.singletonList(num))
            .build();
    }
}
