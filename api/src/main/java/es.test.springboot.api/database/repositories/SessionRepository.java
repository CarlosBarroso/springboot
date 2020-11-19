package es.test.springboot.api.database.repositories;

import es.test.springboot.api.database.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
