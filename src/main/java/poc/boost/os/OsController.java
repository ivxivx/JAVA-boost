package poc.boost.os;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import poc.boost.app.BaseController;
import poc.boost.app.ControllerMetadata;
import poc.boost.app.ControllerType;
import poc.boost.app.ListResponse;

@ControllerMetadata(type = ControllerType.OS)
@RequestMapping("/os")
@RestController
@Slf4j
public class OsController implements BaseController {

    @RequestMapping(value = "/file/grep", method = {RequestMethod.GET, RequestMethod.POST})
    public ListResponse grepFile(@RequestBody GrepFileRequest request) throws IOException {
        //todo mode = local/remote, remote=ssh, local=process
        log.debug("request {}", request);

        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", request.getCommand());
        builder.redirectErrorStream(true);
        
        Process p = builder.start();

        try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            return ListResponse.builder().data(r.lines().collect(Collectors.toList())).build();
        }
    }
}
