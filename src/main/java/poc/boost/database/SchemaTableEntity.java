package poc.boost.database;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SchemaTableEntity {
    private String tableCatalog;
    private String tableSchema;
    private String tableName;
    private String tableType;
    private String engine;
    private String tableComment;
}
