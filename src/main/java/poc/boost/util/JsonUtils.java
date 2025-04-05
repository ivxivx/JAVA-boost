package poc.boost.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import poc.boost.app.BoostRuntimeException;
import java.util.Map;

public class JsonUtils {
    public static Map<String, Object> readAsMap(String value) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(value, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            throw  new BoostRuntimeException("unable to parse value as map", e);
        }
    }

    public static Map<String, Object> readAsMap(byte[] value) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(value, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            throw  new BoostRuntimeException("unable to parse value as map", e);
        }
    }

    public static String writeAsString(Object value) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw  new BoostRuntimeException("unable to write value as string", e);
        }
    }
}
