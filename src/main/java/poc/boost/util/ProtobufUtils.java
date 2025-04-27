package poc.boost.util;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.JsonFormat;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ProtobufUtils {
    public static Map<String, Object> convertToMap(GeneratedMessage message) {
        try {
            val json = JsonFormat.printer().print(message);

            return JsonUtils.readAsMap(json);
        } catch (Exception e) {
            log.error("Error converting Protobuf message to Map", e);
            return new HashMap<>();
        }
    }

    public static Instant convertTimestampToInstance(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }

        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}
