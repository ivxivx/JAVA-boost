package poc.boost.database;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import poc.boost.app.BaseController;
import poc.boost.app.ControllerMetadata;
import poc.boost.app.ControllerType;
import poc.boost.app.ListResponse;

@ControllerMetadata(type = ControllerType.JDBC)
@RequestMapping("/jdbc")
@RestController
@Slf4j
public class JdbcController implements BaseController {
    @Autowired
    private JdbcService jdbcService;

    @RequestMapping(value = "/single/read", method = {RequestMethod.POST})
    public ListResponse read(@RequestBody DatabaseReadRequest request) {
        return ListResponse.builder().data(jdbcService.read(request.getSql())).build();
    }

    @RequestMapping(value = "/single/write", method = {RequestMethod.POST})
    public ListResponse write(@RequestBody DatabaseReadRequest request) {
        String sql = request.getSql();

        return ListResponse.builder().data(Collections.singletonList(jdbcService.write(sql))).build();
    }

    @RequestMapping(value = "/sharding/read", method = {RequestMethod.POST})
    public ListResponse readShard(@RequestBody DatabaseReadRequest request) {
        String sql = request.getSql();

        return ListResponse.builder().data(jdbcService.readSharding(sql)).build();
    }
}
