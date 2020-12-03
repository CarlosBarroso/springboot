package es.test.springboot.worker.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import es.test.springboot.worker.ErrorHandler.AddSessionErrorHandler;
import es.test.springboot.worker.annotations.Log;
import es.test.springboot.worker.database.entities.Session;
import es.test.springboot.worker.transformers.ConfirmationMailTransformer;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.MongoDbFactoryParser;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.mongodb.store.ConfigurableMongoDbMessageStore;
import org.springframework.integration.mongodb.store.MongoDbChannelMessageStore;
import org.springframework.integration.store.BasicMessageGroupStore;
import org.springframework.integration.store.MessageGroupQueue;
import org.springframework.integration.store.MessageGroupStore;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
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
    public MessageChannel addSessionChannel() throws Exception {
//        DirectChannel channel = new DirectChannel();
//        channel.addInterceptor(new WireTap(debugChannel()));
//        return channel;

        return MessageChannels.direct()
//                .wireTap("debugChannel")
                .get();
    }

    /*******mongo************/

    @Value("${spring.data.mongodb.uri}")
    private String MONGODB_URI;

    @Log
    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory ()
    {
        ConnectionString connectionString = new ConnectionString(MONGODB_URI);

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return new SimpleMongoClientDatabaseFactory(MongoClients.create(mongoClientSettings), "test");
    }

    @Log
    @Bean
    MongoDbChannelMessageStore mongoDbChannelMessageStore(MongoDatabaseFactory mongoDatabaseFactory)
    {
        return new MongoDbChannelMessageStore(mongoDatabaseFactory, "message-store");
    }

    @Log
    @Bean
    MessageGroupQueue messageGroupQueue (MongoDbChannelMessageStore mongoDbChannelMessageStore)
    {
        return new MessageGroupQueue(
                mongoDbChannelMessageStore,
                "GroupId");
    }

    @Log
    @Bean
    public MessageChannel debugChannel()  {
        return new QueueChannel(
                messageGroupQueue(
                        mongoDbChannelMessageStore(
                                mongoDatabaseFactory()
                        )
                )
        );
    }

    @Bean
    public MessageChannel eventChannel(){
        return new PublishSubscribeChannel();
    }


}
