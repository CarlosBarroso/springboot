package es.test.springboot.worker.database.repositories;

import es.test.springboot.worker.database.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
