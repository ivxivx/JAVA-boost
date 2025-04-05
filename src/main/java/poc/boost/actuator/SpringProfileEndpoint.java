package poc.boost.actuator;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import poc.boost.app.BaseController;
import poc.boost.app.ControllerMetadata;
import poc.boost.app.ControllerType;
import poc.boost.app.ListResponse;
import poc.boost.spring.SpringService;

@Component
@ControllerMetadata(type = ControllerType.Actuator)
@Endpoint(id="spring-profile")
public class SpringProfileEndpoint implements BaseController {
    @Autowired
    private SpringService springService;

    @Autowired
    private Environment environment;

    @ReadOperation
    public ListResponse read() {
        String[] profiles = environment.getActiveProfiles();

        if (profiles.length == 0) {
            profiles = environment.getDefaultProfiles();
        }

        return ListResponse.builder().data(Arrays.asList(profiles)).build();
    }

    @WriteOperation
    public void update(@Selector String profile) {
        //change to Mono
        //fixme restart kafka client, listen applicatonrefreshevent
        springService.restartApplication(profile);
    }
}
