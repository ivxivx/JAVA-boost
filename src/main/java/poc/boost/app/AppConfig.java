package poc.boost.app;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;

@Configuration
@EnableCaching(proxyTargetClass = true)
@Slf4j
public class AppConfig {
    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                return super.getErrorAttributes(webRequest, options.including(Include.MESSAGE));
            }
        };
    }

    @JsonComponent
    public static class TimestampSerializer extends JsonSerializer<Timestamp> {
        @Override
        public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            //gen.writeNumber(Optional.ofNullable(value).map(Timestamp::getTime).orElse(null));

            gen.writeObject(Optional.ofNullable(value)
                    .map(e -> ZonedDateTime.ofInstant(e.toInstant(), ZoneId.systemDefault()))
                    .orElse(null));
        }
    }

    @JsonComponent
    public static class DateSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeNumber(Optional.ofNullable(value).map(Date::getTime).orElse(0L));
        }
    }

    @JsonComponent
    public static class InstantSerializer extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeObject(Optional.ofNullable(value)
                    .map(e -> ZonedDateTime.ofInstant(e, ZoneId.systemDefault()))
                    .orElse(null));
        }
    }
}
