package si.fri.mag;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("appproperties")
public class MediaMetadataRemoteServiceConfig {
    @ConfigValue(value = "services.mediametadata.url", watch = false)
    private String mediaMetadataUrl;

    @ConfigValue(value = "services.mediametadata.endpoints.newmediametadata", watch = false)
    private String newMediaMetadataUri;

    public String getMediaMetadataUrl() {
        return mediaMetadataUrl;
    }

    public String getNewMediaMetadataUri() {
        return newMediaMetadataUri;
    }

    public void setMediaMetadataUrl(String mediaMetadataUrl) {
        this.mediaMetadataUrl = mediaMetadataUrl;
    }

    public void setNewMediaMetadataUri(String newMediaMetadataUri) {
        this.newMediaMetadataUri = newMediaMetadataUri;
    }
}
