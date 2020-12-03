package es.test.springboot.worker.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import es.test.springboot.worker.ErrorHandler.AddSessionErrorHandler;
import es.test.springboot.worker.annotations.Log;
import es.test.springboot.worker.database.entities.Session;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.mongodb.store.MongoDbChannelMessageStore;
import org.springframework.integration.store.MessageGroupQueue;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;


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
        return MessageChannels
                .direct()
                .get();
    }

    @Bean
    @Transformer(inputChannel = "fromRabbit", outputChannel = "addSessionChannel")
    public JsonToObjectTransformer jsonToObjectTransformer() {
        return new JsonToObjectTransformer(Session.class);
    }

    @Bean
    public MessageChannel addSessionChannel() throws Exception {
        return MessageChannels.direct()
                .interceptor(new WireTap(debugChannel()))
                .get();
    }

    /*******mongo************/

    @Value("${spring.data.mongodb.uri}")
    private String MONGODB_URI;

    @Value("${spring.data.mongodb.database}")
    private String MONGODB_DATABASE_NAME;

    @Log
    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory ()
    {
        String connectionString = new StringBuilder()
                .append(MONGODB_URI)
                .append("?uuidRepresentation=STANDARD")
                .toString();

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .build();

        return new SimpleMongoClientDatabaseFactory(MongoClients.create(mongoClientSettings), MONGODB_DATABASE_NAME);
    }

    /*
    @Bean
    public MongoCustomConversions mongoCustomConversion() {
        return new MongoCustomConversions(Arrays.asList(
                new MediaTypeToStringConverter(),
                new StringToMediaTypeConverter(),
                new OffsetDateTimetoStringConverter(),
                new StringToOffsetDateTimeConverter()
        ));
    }

     */
    @Log
    @Bean
    MongoDbChannelMessageStore mongoDbChannelMessageStore(MongoDatabaseFactory mongoDatabaseFactory)
    {
        return new MongoDbChannelMessageStore(mongoDatabaseFactory, "messages");
    }

    @Log
    @Bean
    public MessageChannel debugChannel()  {
        return MessageChannels
                .queue(
                        mongoDbChannelMessageStore(
                                mongoDatabaseFactory()
                        ),
                        "GroupId")
                .get();
    }

    @Bean
    public MessageChannel eventChannel(){
        return MessageChannels
                .publishSubscribe()
                .get();
    }


}
