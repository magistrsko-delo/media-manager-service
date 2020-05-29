package si.fri.mag;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("appproperties")
public class MediaChunksRemoteServiceConfig {
    @ConfigValue(value = "services.mediachunks.url", watch = false)
    private String mediaChunksUrl;

    public String getMediaChunksUrl() {
        return mediaChunksUrl;
    }

    public void setMediaChunksUrl(String mediaChunksUrl) {
        this.mediaChunksUrl = mediaChunksUrl;
    }
}
