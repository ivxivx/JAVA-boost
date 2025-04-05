package poc.boost.demo;

import lombok.extern.slf4j.Slf4j;
import poc.boost.config.ConfigService;
import poc.boost.redis.RedisOpsForHashSetRequest;
import poc.boost.redis.StringRedisService;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class DemoService {
    private static final String SQL_CREATE_ORDER_TABLE = """
        CREATE TABLE IF NOT EXISTS orders (
            id VARCHAR(255) PRIMARY KEY,
            external_id VARCHAR(255),
            amount DECIMAL(20, 6),
            status VARCHAR(255)
        );
        """;

    private static final String SQL_INSERT_ORDER = """
        INSERT INTO orders (id, external_id, amount, status)
        VALUES (?, ?, ?, ?)
        """;
    
    @Autowired
    private ConfigService configService;

    @Autowired
    private StringRedisService  stringRedisService;

    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        log.info("creating order");

        configService.getJdbcTemplate().update(SQL_CREATE_ORDER_TABLE);

        String orderId = "order_" + System.currentTimeMillis();
        String status;

        if (request.getAmount().compareTo(new BigDecimal(100)) > 0) {
            status = "completed";
        } else {
            status = "failed";
        }

        configService.getJdbcTemplate().update(SQL_INSERT_ORDER, 
            orderId, request.getExternal_id(), request.getAmount(), status);

        log.info("order {}: {}, {}", status, request.getExternal_id(), orderId);

        CreateOrderResponse response = CreateOrderResponse.builder()
            .order_id(orderId)
            .status(status)
            .build();

        Map<String, Object> orderData = Map.of(
            "id", orderId,
            "external_id", request.getExternal_id(),
            "amount", request.getAmount(),
            "status", status
        );

        stringRedisService.opsForHashSet(RedisOpsForHashSetRequest.builder()
            .key("order:" + orderId)
            .hashKey(orderId)
            .hashValue(orderData)
            .build());

        return response;
    }
}