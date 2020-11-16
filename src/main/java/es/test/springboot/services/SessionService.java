package es.test.springboot.services;

import es.test.springboot.entities.Session;

public interface SessionService {
    Session add(Session session);

    Session update (Session session);
}
