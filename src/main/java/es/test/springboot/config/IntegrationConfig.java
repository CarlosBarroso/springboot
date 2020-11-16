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

    @Bean
    public MessageChannel messageChannelUpdateSession()
    {
        return MessageChannels.direct("messageChannelUpdateSession").get();
    }

    @Bean
    public IntegrationFlow integrationFlowUpdate (SessionService sessionService)
    {
        return IntegrationFlows.from("messageChannelUpdateSession")
                .handle(sessionService, "update")
                .get();
    }
}
