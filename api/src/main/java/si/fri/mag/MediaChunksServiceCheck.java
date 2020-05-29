package si.fri.mag;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.inject.spi.CDI;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class MediaChunksServiceCheck implements HealthCheck {
    private static final Logger LOG = Logger.getLogger(MediaChunksServiceCheck.class.getSimpleName());
    @Override
    public HealthCheckResponse call() {
        MediaChunksRemoteServiceConfig configMediaChunks = CDI.current().select(MediaChunksRemoteServiceConfig.class).get();

        // System.out.println(configMediaChunks.getMediaChunksUrl() + "health");

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(configMediaChunks.getMediaChunksUrl() + "health").openConnection();
            connection.setRequestMethod("HEAD");

            if (connection.getResponseCode() == 200) {
                return HealthCheckResponse.builder().withData("url", configMediaChunks.getMediaChunksUrl() + "health").name(MediaChunksServiceCheck.class.getSimpleName()).up().build();
            }
        } catch (Exception exception) {
            LOG.severe(exception.getMessage());
        }
        return HealthCheckResponse.builder().withData("url", configMediaChunks.getMediaChunksUrl() + "health").name(MediaChunksServiceCheck.class.getSimpleName()).down().build();
    }
}
