package si.fri.mag;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("appproperties")
public class RabbitMQConfig {
    @ConfigValue(value = "rabbitmq.chunks-queue-name", watch = false)
    private String chunksQueueName;

    @ConfigValue(value = "rabbitmq.image-queue-name", watch = false)
    private String imageQueueName;

    @ConfigValue(value = "rabbitmq.host", watch = true)
    private String host;

    @ConfigValue(value = "rabbitmq.username", watch = false)
    private String username;

    @ConfigValue(value = "rabbitmq.password", watch = false)
    private String password;

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public String getChunksQueueName() {
        return chunksQueueName;
    }

    public String getImageQueueName() {
        return imageQueueName;
    }

    public String getUsername() {
        return username;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setChunksQueueName(String chunksQueueName) {
        this.chunksQueueName = chunksQueueName;
    }

    public void setImageQueueName(String imageQueueName) {
        this.imageQueueName = imageQueueName;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
