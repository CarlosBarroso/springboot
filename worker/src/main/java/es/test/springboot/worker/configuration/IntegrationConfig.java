package es.test.springboot.worker.configuration;

import es.test.springboot.worker.ErrorHandler.AddSessionErrorHandler;
import es.test.springboot.worker.database.entities.Session;
import es.test.springboot.worker.transformers.ConfirmationMailTransformer;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.mongodb.store.MongoDbChannelMessageStore;
import org.springframework.integration.store.BasicMessageGroupStore;
import org.springframework.integration.store.MessageGroupQueue;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class IntegrationConfig {

    @Value("${spring.rabbitmq.queue}")
    private String rabbitMQ_queue;

    @Autowired
    private AddSessionErrorHandler addSessionErrorHandler;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public AbstractMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        messageListenerContainer.setQueueNames(rabbitMQ_queue);
        messageListenerContainer.setErrorHandler(addSessionErrorHandler);
        messageListenerContainer.setChannelTransacted(true);
        messageListenerContainer.setTransactionManager(transactionManager);
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
        DirectChannel channel = new DirectChannel();
        channel.addInterceptor(new WireTap(debugChannel()));
        return channel;
    }

    @Autowired MongoDbFactory mongoDatabaseFactory;
    private static final String GROUP_ID= "my-group";

    @Bean
    public MongoDbChannelMessageStore mongoDbChannelMessageStore (MongoDbFactory mongoDatabaseFactory)
    {
        return new MongoDbChannelMessageStore(mongoDatabaseFactory, "message-store");
    }

    @Bean
    public MessageChannel debugChannel(){
        return new QueueChannel(
                new MessageGroupQueue(
                        mongoDbChannelMessageStore(mongoDatabaseFactory), GROUP_ID));
    }

    @Bean
    public MessageChannel eventChannel(){
        return new PublishSubscribeChannel();
    }

    @Autowired
    private ConfirmationMailTransformer confirmationMailTransformer;

    @Value("${email.host}")
    private String EMAIL_HOST;
    @Value("${email.port}")
    private String EMAIL_PORT;
    @Value("${email.user}")
    private String EMAIL_USER;

    @Bean
    public IntegrationFlow myFlow() {
        return IntegrationFlows.from("eventChannel")
                .enrichHeaders(Mail.headers()
                        .subjectFunction(m -> "asunto del mensaje")
                        .from(EMAIL_USER)
                        .toFunction(m -> new String[] { "bar@baz" }))
                .transform(confirmationMailTransformer, "toMailText")
                .handle(Mail.outboundAdapter(EMAIL_HOST)
                        .port(Integer.parseInt(EMAIL_PORT))
//                        .credentials(EMAIL_USER, EMAIL_PASSWORD)
                        .protocol("smtp")
                )
                .get();
    }


}
