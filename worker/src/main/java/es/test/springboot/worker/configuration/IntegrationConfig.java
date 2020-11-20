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
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.messaging.MessageChannel;

import java.util.HashMap;
import java.util.Map;

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

    @Bean
    public MessageChannel registrationEvent()
    {
        return new PublishSubscribeChannel();
    }
/*
 @Bean
    public IntegrationFlow integrationFlowAdd (SessionService sessionService)
    {
        return IntegrationFlows.from("messageChannelAddSession")
                .handle(sessionService, "add")
                .get();
    }
     <int-mail:header-enricher>
            <int-mail:from value="registration@globomantics.com"/>
            <int-mail:to expression="payload.attendeeEmail"/>
            <int-mail:subject value="Confirmation of your registration for the Globomantics Tech Conference"/>
        </int-mail:header-enricher>

        <!-- Transform payload to text to be used as the body of the e-mail -->
        <int:transformer ref="confirmationMailTransformer" method="toMailText"/>

        <!-- Outbound channel adapter for sending e-mail -->
        <int-mail:outbound-channel-adapter host="localhost" port="3025" username="globomantics" password="globomail"/>
 */
    /*
        @Bean
        public HeaderEnricher mailHeader ()
        {
            Map<String, String> header = new HashMap<String, String>();
            header.put("from","from@ejemplo.com");
            header.put("to","to@ejemplo.com");
            header.put("subject","subject del mail");
            return  new HeaderEnricher(header);

        }
*/
        //https://github.com/spring-projects/spring-integration-java-dsl/wiki/Spring-Integration-Java-DSL-Reference#message-channels
        //https://github.com/spring-projects/spring-integration-java-dsl/wiki/Spring-Integration-Java-DSL-Reference#sub-flows-support
        //https://www.javacodegeeks.com/2015/09/spring-integration-fundamentals.html
        /* https://docs.spring.io/spring-integration/reference/html/mail.html
        @Bean
        public IntegrationFlow sendMailFlow() {
        return IntegrationFlows.from("sendMailChannel")
                .enrichHeaders(Mail.headers()
                        .subjectFunction(m -> "foo")
                        .from("foo@bar")
                        .toFunction(m -> new String[] { "bar@baz" }))
                .handle(Mail.outboundAdapter("gmail")
                            .port(smtpServer.getPort())
                            .credentials("user", "pw")
                            .protocol("smtp")),
                    e -> e.id("sendMailEndpoint"))
                .get();*/
/* wip
        @Bean
        public IntegrationFlow classify() {
            return IntegrationFlows.from("registrationEvent")
                                    .transform(mailHeader())
                                    .transform("confirmationMailTransformer","toMailText")
                                    .channel("registrationEvent")
                                    .get();
        }
*/
}
