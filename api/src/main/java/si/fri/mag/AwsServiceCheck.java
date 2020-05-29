package si.fri.mag;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.inject.spi.CDI;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class AwsServiceCheck implements HealthCheck {

    private static final Logger LOG = Logger.getLogger(AwsServiceCheck.class.getSimpleName());

    @Override
    public HealthCheckResponse call() {
        AWSRemoteServiceConfig awsRemoteServiceConfig = CDI.current().select(AWSRemoteServiceConfig.class).get();

        // System.out.println(awsRemoteServiceConfig.getAwsStorageUrl() + "health");

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(awsRemoteServiceConfig.getAwsStorageUrl() + "health").openConnection();
            connection.setRequestMethod("HEAD");

            if (connection.getResponseCode() == 200) {
                return HealthCheckResponse.builder().withData("url", awsRemoteServiceConfig.getAwsStorageUrl() + "health").name(AwsServiceCheck.class.getSimpleName()).up().build();
            }
        } catch (Exception exception) {
            LOG.severe(exception.getMessage());
        }
        return HealthCheckResponse.builder().withData("url", awsRemoteServiceConfig.getAwsStorageUrl() + "health").name(AwsServiceCheck.class.getSimpleName()).down().build();
    }
}
