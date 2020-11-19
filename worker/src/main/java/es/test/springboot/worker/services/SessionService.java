package es.test.springboot.worker.services;

import es.test.springboot.worker.database.entities.Session;
import es.test.springboot.worker.database.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    SessionRepository sessionRepository;

    @Override
    public Session add(@Payload Session session)
    {
       return sessionRepository.saveAndFlush(session);
    }

    @Override
    public Session update(Session session) {
        return sessionRepository.saveAndFlush(session);
    }


}