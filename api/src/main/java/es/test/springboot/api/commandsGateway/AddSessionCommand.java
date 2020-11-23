package es.test.springboot.api.commandsGateway;

import es.test.springboot.api.database.entities.Session;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(name="addSessionCommand", defaultRequestChannel = "addSessionRequestChannel")
public interface AddSessionCommand {

    @Gateway
    void execute (Message<Session> session);
}
