package poc.boost.actuator;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.springframework.boot.actuate.health.AbstractReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.data.redis.connection.ClusterInfo;
import org.springframework.data.redis.connection.ReactiveRedisClusterConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class RedisHealthIndicator extends AbstractReactiveHealthIndicator {
    private final ReactiveRedisConnectionFactory connectionFactory;

//    private final List<String> infoSections = Arrays.asList("server", "clients", "cpu", "memory", "persistence", "stats", "commandstats", "latencystats",
//            "keyspace", "errorstats", "modules", "cluster", "replication");

    public RedisHealthIndicator(ReactiveRedisConnectionFactory connectionFactory) {
        super("Redis health check failed");
        this.connectionFactory = connectionFactory;
    }

    @Override
    protected Mono<Health> doHealthCheck(Health.Builder builder) {
        return getConnection().flatMap((connection) -> doHealthCheck(builder, connection));
    }

    private Mono<ReactiveRedisConnection> getConnection() {
        return Mono.fromSupplier(this.connectionFactory::getReactiveConnection)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Health> doHealthCheck(Health.Builder builder, ReactiveRedisConnection connection) {
        return getHealth(builder, connection).onErrorResume((ex) -> Mono.just(builder.down(ex).build()))
                .flatMap((health) -> connection.closeLater().thenReturn(health));
    }

    private Mono<Health> getHealth(Health.Builder builder, ReactiveRedisConnection connection) {
        if (connection instanceof ReactiveRedisClusterConnection) {
            return ((ReactiveRedisClusterConnection) connection).clusterGetClusterInfo()
                    .map((info) -> fromClusterInfo(builder, info).build());
        }
        return connection.serverCommands().info().map((info) -> up(builder, info).build());
    }

    private Builder up(Health.Builder builder, Properties info) {
        Map<String, Object> map = new TreeMap<>();
        info.forEach( (k,v) -> map.put(k.toString(), v));

        builder.withDetails(map);
        return builder.up();
    }

    private Builder fromClusterInfo(Health.Builder builder, ClusterInfo clusterInfo) {
        builder.withDetail("cluster_size", clusterInfo.getClusterSize());
        builder.withDetail("slots_up", clusterInfo.getSlotsOk());
        builder.withDetail("slots_fail", clusterInfo.getSlotsFail());

        if ("fail".equalsIgnoreCase(clusterInfo.getState())) {
            return builder.down();
        }
        else {
            return builder.up();
        }
    }
}
