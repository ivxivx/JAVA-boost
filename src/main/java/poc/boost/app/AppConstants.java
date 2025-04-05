package poc.boost.app;

public interface AppConstants {
    String HTTP_HEADER_REDIRECT_URL = "x-boost-redirect-url";

    String DEV_HIKARI_CONFIG = "devHikariConfig";
    String DEV_DATA_SOURCE = "devDataSource";
    String DEV_JDBC_TEMPLATE = "devJavaTemplate";

    String QA_HIKARI_CONFIG = "qaHikariConfig";
    String QA_DATA_SOURCE = "qaDataSource";
    String QA_JDBC_TEMPLATE = "qaJavaTemplate";

    String SSH_DATA_SOURCE_SERVICE = "sshDataSourceService";
    String QA_DATA_SOURCE_SSH_CONFIG = "qaDataSourceSshConfig";
    String QA_DATA_SOURCE_SSH_FORWARDING_CONFIG = "qaDataSourceSshForwardingConfig";

    String SSH_JUMP_SERVER_SERVICE = "sshJumpServerService";
    String SSH_JUMP_SERVER_CONFIG = "jumpServerSshConfig";

    String DEV_KAFKA_CONFIG = "devKafkaConfig";

    String DB_CACHE_MANAGER = "dbCacheManager";
    String DB_CACHE = "dbCache";

    String KAFKA_CACHE_MANAGER = "kafkaCacheManager";
    String KAFKA_CACHE = "kafkaCache";

    String JWT_CACHE_MANAGER = "jwtCacheManager";
    String JWT_CACHE = "jwtCache";
}
