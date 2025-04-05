package poc.boost.database;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import poc.boost.app.BoostRuntimeException;
import poc.boost.config.ConfigService;

@Component
@Slf4j
public class JdbcService {
    private static final String SELECT_SHARDING_TABLE_FORMAT = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE LOWER(TABLE_SCHEMA)='%s' AND LOWER(TABLE_NAME) LIKE '%s%%'";
    private static final String SCHEMA_NAME = "__schema__";
    private static final String TABLE_NAME = "__table__";

    @Autowired
    private ConfigService configService;

    //@Cacheable(key = "{#p0}", cacheNames = {AppConstants.DB_CACHE}, cacheManager = AppConstants.DB_CACHE_MANAGER)
    public List<Map<String, Object>> read(String sql) {
        List<Map<String, Object>> result = getJdbcTemplate().queryForList(sql);

        log.info("sql: {}, result: {}", sql, result);

        return result;
    }

    public int write(String sql) {
        int result = getJdbcTemplate().update(sql);

        log.info("sql: {}, result: {}", sql, result);

        return result;
    }

    public List<Map<String, Object>> readSharding(String sql) {
        SelectStatement selectStatement = SelectStatement.match(sql);

        String selectShardingTableNameSql = String.format(SELECT_SHARDING_TABLE_FORMAT, selectStatement.getSchemaName(), selectStatement.getTableName());
        List<SchemaTableEntity> tables = getJdbcTemplate().query(selectShardingTableNameSql, (rs, rowNum) -> SchemaTableEntity.builder()
            .tableCatalog(rs.getString("table_catalog"))
            .tableSchema(rs.getString("table_schema"))
            .tableName(rs.getString("table_name"))
            .tableType(rs.getString("table_type"))
            .engine(rs.getString("engine"))
            .tableComment(rs.getString("table_comment"))
            .build());

        if (tables.isEmpty()) {
            throw new BoostRuntimeException("invalid sharding sql, cannot find table names, sql=" + sql);
        }

        List<Map<String, Object>> result = tables.stream().flatMap(table -> {
            String shardingSql = "SELECT * FROM " + table.getTableSchema() + "." + table.getTableName() + " " + selectStatement.getSuffix();
            List<Map<String, Object>> oneTableResult = getJdbcTemplate().queryForList(shardingSql);

            oneTableResult.forEach(ret -> {
                ret.put(SCHEMA_NAME, table.getTableSchema());
                ret.put(TABLE_NAME, table.getTableName());
            });

            return Stream.of(oneTableResult);
        }).flatMap(List::stream)
        .collect(Collectors.toList());

        log.info("sql: {}, result: {}", sql, result);

        return result;
    }

//    private List<Map<String, Object>> readSharding(String schemaName, String tableName, List<String> sqls) {
//         return sqls.stream().flatMap(sql -> {
//            List<Map<String, Object>> oneTableResult = getJdbcTemplate().queryForList(sql);
//
//            return Stream.of(oneTableResult);
//        }).flatMap(List::stream)
//        .collect(Collectors.toList());
//    }

    private JdbcTemplate getJdbcTemplate() {
        return configService.getJdbcTemplate();
    }

    @Builder
    @Data
    public static class SelectStatement {
        //private static final String SELECT_STATEMENT_PATTEN_STRING = "\\s*SELECT\\s+[A-Za-z0-9_$*]+\\s+FROM\\s+([A-Za-z0-9_$]+)\\.([A-Za-z0-9_$]+)\\s+[A-Za-z0-9_$\\s]+";
        private static final String SELECT_STATEMENT_PATTEN_STRING = "\\s+(.+)FROM\\s+([A-Za-z0-9_]+)\\.([A-Za-z0-9_]+)(.*)";
        private static final Pattern SELECT_STATEMENT_PATTEN = Pattern.compile(SELECT_STATEMENT_PATTEN_STRING, Pattern.CASE_INSENSITIVE);

        private String prefix;
        private String schemaName;
        private String tableName;
        private String suffix;

        public static SelectStatement match(String sql) {
            Matcher matcher = SELECT_STATEMENT_PATTEN.matcher(sql.toLowerCase(Locale.ROOT));

            SelectStatement selectStatement = null;

            if (matcher.find()) {
                //only one match group
                selectStatement = SelectStatement.builder()
                    .prefix(matcher.group(1))
                    .schemaName(matcher.group(2))
                    .tableName(matcher.group(3))
                    .suffix(matcher.group(4))
                    .build();
            }

            if (selectStatement == null || !selectStatement.isValid()) {
                throw new BoostRuntimeException("invalid sharding sql, sql=" + sql);
            }

            return selectStatement;
        }

        private boolean isValid() {
            return StringUtils.hasText(prefix) && StringUtils.hasText(schemaName) && StringUtils.hasText(tableName);
        }
    }
}
