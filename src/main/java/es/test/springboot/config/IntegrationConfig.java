package es.test.springboot.config;

import es.test.springboot.services.SessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannel messageChannel()
    {
        return MessageChannels.direct("addSessionRequest").get();
    }

    @Bean
    public IntegrationFlow integrationFlow (SessionService sessionService)
    {
        return IntegrationFlows.from("addSessionRequest")
                .handle(sessionService, "add")
                .get();
    }
}
