package es.test.springboot.worker.configuration;

import es.test.springboot.worker.database.entities.Session;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationConfig {

    @Value("${spring.rabbitmq.queue}")
    private String rabbitMQ_queue;


    @Bean
    public AbstractMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        messageListenerContainer.setQueueNames(rabbitMQ_queue);
        return messageListenerContainer;
    }

    @Bean
    public AmqpInboundChannelAdapter inboundChannelAdapter(AbstractMessageListenerContainer messageListenerContainer) {
        AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(messageListenerContainer);
        adapter.setOutputChannelName("fromRabbit");
        return adapter;
    }

    @Bean
    public MessageChannel fromRabbit() {
        return new DirectChannel();
    }

    @Bean
    @Transformer(inputChannel = "fromRabbit", outputChannel = "registrationRequest")
    public JsonToObjectTransformer jsonToObjectTransformer() {
        return new JsonToObjectTransformer(Session.class);
    }

    @Bean
    public MessageChannel registrationRequest() {
        return new DirectChannel();
    }
}
