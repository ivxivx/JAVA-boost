package poc.boost.actuator;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.util.unit.DataSize;

@Slf4j
public class DiskSpaceHealthIndicator  extends AbstractHealthIndicator {
    private final File path;
    private final DataSize threshold;

    /**
     * Create a new {@code DiskSpaceHealthIndicator} instance.
     * @param path the Path used to compute the available disk space
     * @param threshold the minimum disk space that should be available
     */
    public DiskSpaceHealthIndicator(File path, DataSize threshold) {
        super("DiskSpace health check failed");
        this.path = path;
        this.threshold = threshold;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        long diskFreeInBytes = this.path.getUsableSpace();
        if (diskFreeInBytes >= this.threshold.toBytes()) {
            builder.up();
        }
        else {
            log.warn("Free disk space below threshold. Available: {} bytes (threshold: {})", diskFreeInBytes, this.threshold);
            builder.down();
        }

        long totalSpace = this.path.getTotalSpace();

        builder.withDetail("total", totalSpace)
                .withDetail("free", diskFreeInBytes)
                .withDetail("usage", diskFreeInBytes * 10000 / totalSpace / 100.0)
                .withDetail("threshold", this.threshold.toBytes())
                .withDetail("exists", this.path.exists());
    }
}
