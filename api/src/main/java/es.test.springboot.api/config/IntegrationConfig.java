package es.test.springboot.api.config;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

@Configuration
public class IntegrationConfig {
/* v1
    @Bean(name="messageChannelAddSession")
    public MessageChannel messageChannelAddSession()
    {
        return MessageChannels.direct("messageChannelAddSession").get();
    }

    @Bean
    public IntegrationFlow integrationFlowAdd (SessionService sessionService)
    {
        return IntegrationFlows.from("messageChannelAddSession")
                .handle(sessionService, "add")
                .get();
    }

    @Bean(name="messageChannelUpdateSession")
    public MessageChannel messageChannelUpdateSession()
    {
        return MessageChannels.direct("messageChannelUpdateSession").get();
    }
*/

    //rabbitmq
    @Value("${spring.rabbitmq.queue}")
    private String rabbitMQ_queue;

    //application channel
    @Bean(name="addSessionRequestChannel")
    public MessageChannel addSessionRequestChannel() {
        return MessageChannels
                .direct()
                .get();
    }

    //to rabbit mq transformer
    @Bean
    @Transformer(inputChannel = "addSessionRequestChannel", outputChannel = "toRabbit")
    public ObjectToJsonTransformer objectToJsonTransformer() {
        return new ObjectToJsonTransformer();
    }

    @Bean
    public SubscribableChannel toRabbit() {
        return MessageChannels
                .direct()
                .get();
    }

    @Bean
    public EventDrivenConsumer rabbitConsumer(@Qualifier("toRabbit") SubscribableChannel channel,
                                              @Qualifier("rabbitOutboundEndpoint") MessageHandler handler) {
        return new EventDrivenConsumer(channel, handler);
    }

    @Bean
    public AmqpOutboundEndpoint rabbitOutboundEndpoint(AmqpTemplate amqpTemplate) {
        AmqpOutboundEndpoint adapter = new AmqpOutboundEndpoint(amqpTemplate);
        adapter.setRoutingKey(rabbitMQ_queue);
        return adapter;
    }
}
