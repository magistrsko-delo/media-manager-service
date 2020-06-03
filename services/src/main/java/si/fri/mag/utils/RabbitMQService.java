package si.fri.mag.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import si.fri.mag.RabbitMQConfig;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@ApplicationScoped
public class RabbitMQService {
    private static String CHUNKS_QUEUE_NAME = "";
    private static String IMAGE_QUEUE_NAME = "";
    private static String VIDEO_ANALYSIS_QUEUE_NAME = "";

    @Inject
    private RabbitMQConfig rabbitMQConfig;

    private ConnectionFactory factory;

    @PostConstruct
    private void init(){
        CHUNKS_QUEUE_NAME = rabbitMQConfig.getChunksQueueName();
        IMAGE_QUEUE_NAME = rabbitMQConfig.getImageQueueName();
        VIDEO_ANALYSIS_QUEUE_NAME = rabbitMQConfig.getVideoAnalysisQueue();
        factory = new ConnectionFactory();
        factory.setHost(rabbitMQConfig.getHost());
        factory.setUsername(rabbitMQConfig.getUsername());
        factory.setPassword(rabbitMQConfig.getPassword());
    }

    public void sendChunksQueueMessage(String message) {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()
        )
        {
            channel.queueDeclare(CHUNKS_QUEUE_NAME, true, false, false, null);

            channel.basicPublish("", CHUNKS_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
        } catch (TimeoutException | IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void sendImageQueueMessage(String message) {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()
        )
        {
            channel.queueDeclare(IMAGE_QUEUE_NAME, true, false, false, null);

            channel.basicPublish("", IMAGE_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
        } catch (TimeoutException | IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void sendVideoAnalysisQueueMessage(String message) {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()
        )
        {
            channel.queueDeclare(VIDEO_ANALYSIS_QUEUE_NAME, true, false, false, null);

            channel.basicPublish("", VIDEO_ANALYSIS_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
        } catch (TimeoutException | IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
