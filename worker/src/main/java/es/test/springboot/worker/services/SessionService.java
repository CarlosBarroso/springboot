package es.test.springboot.worker.services;

import es.test.springboot.worker.annotations.Log;
import es.test.springboot.worker.database.entities.Session;
import es.test.springboot.worker.database.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class SessionService  {

    @Autowired
    SessionRepository sessionRepository;

    @Log
    @ServiceActivator(inputChannel = "addSessionChannel")
    public void add(@Payload Session session)
    {
        sessionRepository.saveAndFlush(session);
    }

    public Session update(Session session) {
        return sessionRepository.saveAndFlush(session);
    }


}
