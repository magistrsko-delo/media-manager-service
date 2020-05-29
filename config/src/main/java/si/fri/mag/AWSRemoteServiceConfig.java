package si.fri.mag;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("appproperties")
public class AWSRemoteServiceConfig {

    @ConfigValue(value = "services.awsstorage.url", watch = false)
    private String awsStorageUrl;

    @ConfigValue(value = "services.awsstorage.endpoints.uploadmedia", watch = false)
    private String uploadMediaUri;

    @ConfigValue(value = "services.awsstorage.endpoints.createbucket", watch = false)
    private String createBucketUri;

    @ConfigValue(value = "services.awsstorage.endpoints.getmedia", watch = false)
    private String getMediaUri;

    public String getAwsStorageUrl() {
        return awsStorageUrl;
    }

    public String getCreateBucketUri() {
        return createBucketUri;
    }

    public String getGetMediaUri() {
        return getMediaUri;
    }

    public String getUploadMediaUri() {
        return uploadMediaUri;
    }

    public void setAwsStorageUrl(String awsStorageUrl) {
        this.awsStorageUrl = awsStorageUrl;
    }

    public void setCreateBucketUri(String createBucketUri) {
        this.createBucketUri = createBucketUri;
    }

    public void setGetMediaUri(String getMediaUri) {
        this.getMediaUri = getMediaUri;
    }

    public void setUploadMediaUri(String uploadMediaUri) {
        this.uploadMediaUri = uploadMediaUri;
    }
}
