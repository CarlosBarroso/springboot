package es.test.springboot.worker.configuration;

import es.test.springboot.worker.transformers.ConfirmationMailTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.messaging.MessageChannel;

@Configuration
public class EmailIntegrationConfig {

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
