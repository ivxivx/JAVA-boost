package poc.boost.config;

import java.util.Map;

import lombok.Getter;

@Getter
public class ConfigFile {
    // namespace -> jdbc properties
    private Map<Namespace, Property> jdbc;

    private Map<Namespace, Property> redis;

    private Map<Namespace, Property> temporalio;
}
