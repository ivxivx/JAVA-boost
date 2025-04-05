package poc.boost.database;

import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.val;

public class JdbcConfig {
  public static JdbcTemplate createConfig(Properties properties) {
    val hikariConfig = new HikariConfig(properties);
    val dataSource = new HikariDataSource(hikariConfig);
    val jdbcTemplate = new JdbcTemplate(dataSource);

    return jdbcTemplate;
  }
}
