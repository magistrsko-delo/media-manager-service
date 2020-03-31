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
    private static String TASK_QUEUE_NAME = "";

    @Inject
    private RabbitMQConfig rabbitMQConfig;

    private ConnectionFactory factory;

    @PostConstruct
    private void init(){
        TASK_QUEUE_NAME = rabbitMQConfig.getTaksQueueName();
        factory = new ConnectionFactory();
        factory.setHost(rabbitMQConfig.getHost());
        factory.setUsername(rabbitMQConfig.getUsername());
        factory.setPassword(rabbitMQConfig.getPassword());
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
