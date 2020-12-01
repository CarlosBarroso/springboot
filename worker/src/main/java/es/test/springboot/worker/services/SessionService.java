package es.test.springboot.worker.services;

import es.test.springboot.worker.annotations.Log;
import es.test.springboot.worker.annotations.MetricCounter;
import es.test.springboot.worker.database.entities.Session;
import es.test.springboot.worker.database.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class SessionService  {

    @Autowired
    SessionRepository sessionRepository;

    @Log
    @MetricCounter
    @ServiceActivator(inputChannel = "addSessionChannel", outputChannel = "eventChannel")
    public Message<Session> add(@Payload Session session)
    {
        Session result = sessionRepository.saveAndFlush(session);

        Message<Session> message = MessageBuilder.withPayload(result).build();
        return message;
    }

    public Session update(Session session) {
        return sessionRepository.saveAndFlush(session);
    }


}
