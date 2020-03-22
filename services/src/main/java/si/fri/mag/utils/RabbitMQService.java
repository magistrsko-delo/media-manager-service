package si.fri.mag.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RequestScoped
public class RabbitMQService {
    private static final String TASK_QUEUE_NAME = "MEDIA_CHUNKS_QUEUE";

    ConnectionFactory factory;

    @PostConstruct
    private void init(){
        factory = new ConnectionFactory();
        factory.setHost("172.18.29.145");  // TODO CHANGE
        factory.setUsername("uros");
        factory.setPassword("uros123");
    }

    public void sendMessage(String message) {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()
        )
        {
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            channel.basicPublish("", TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
        } catch (TimeoutException | IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
