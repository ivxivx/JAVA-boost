package poc.boost.config;

import java.io.StringReader;
import java.util.Properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Property {
    private String value;

    public Properties convertToProperties() {
        Properties properties = new Properties();

        try (StringReader reader = new StringReader(value)) {
            properties.load(reader);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert to Properties", e);
        }

        return properties;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }

        Property other = (Property) obj;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
