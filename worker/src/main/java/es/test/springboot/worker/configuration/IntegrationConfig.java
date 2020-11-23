package es.test.springboot.worker.configuration;

import es.test.springboot.worker.database.entities.Session;
import es.test.springboot.worker.transformers.ConfirmationMailTransformer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.transformer.HeaderEnricher;
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
    @Transformer(inputChannel = "fromRabbit", outputChannel = "addSessionChannel")
    public JsonToObjectTransformer jsonToObjectTransformer() {
        return new JsonToObjectTransformer(Session.class);
    }

    @Bean
    public MessageChannel addSessionChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel registrationEvent()
    {
        return new PublishSubscribeChannel();
    }

    @Bean
    public MessageChannel eventChannel(){
        return new PublishSubscribeChannel();
    }

    @Autowired
    private ConfirmationMailTransformer confirmationMailTransformer;

    @Bean
    public IntegrationFlow myFlow() {
        return IntegrationFlows.from("eventChannel")
                .enrichHeaders(h -> h.header("from", "from@test.com") )
                .enrichHeaders(h -> h.header("to", "to@test.com") )
                .enrichHeaders(h -> h.header("subject", "mail confirmación") )
                .transform(confirmationMailTransformer, "toMailText")
                .handle(Http.outboundGateway("http://greenmail:8083")
                        .httpMethod(HttpMethod.POST)
//                        .expectedResponseType(String.class)
                )
                .get();
    }

    /*

    @Bean
    public MessageChannel outMailChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = "httpOutRequest")
    @Bean
    public HttpRequestExecutingMessageHandler outbound() {
        HttpRequestExecutingMessageHandler handler =
                new HttpRequestExecutingMessageHandler("http://localhost:8080/foo");
        handler.setHttpMethod(HttpMethod.POST);
        handler.setExpectedResponseType(String.class);
        return handler;
    }

     */

}
