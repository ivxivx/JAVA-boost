package poc.boost.environment;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnvironmentType {
    DEV(Names.DEV),
    QA(Names.QA),
    ;

    public class Names {
        public static final String DEV = "dev";
        public static final String QA = "qa";
    }
    
    private final String code;

    public static EnvironmentType fromCode(String code) {
        return Arrays.stream(EnvironmentType.values())
            .filter(x -> x.code.equalsIgnoreCase(code))
            .findFirst()
            .orElse(null);
    }
}
