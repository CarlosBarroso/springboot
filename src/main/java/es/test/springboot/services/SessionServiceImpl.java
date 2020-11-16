package es.test.springboot.services;

import es.test.springboot.entities.Session;
import es.test.springboot.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class SessionServiceImpl implements SessionService {

    @Autowired
    SessionRepository sessionRepository;

    @Override
    @ServiceActivator(inputChannel = "addSessionRequest")
    public Session add(Session session)
    {
       return sessionRepository.saveAndFlush(session);
    }

    @Override
    public Session update(Session session) {
        return sessionRepository.saveAndFlush(session);
    }


}