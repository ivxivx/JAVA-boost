package poc.boost.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Data
@NoArgsConstructor
@SuperBuilder
@ToString
public class Response<T> {
    private T data;
}
