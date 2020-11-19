package es.test.springboot.worker.database.repositories;

import es.test.springboot.worker.database.entities.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeakerRepository extends  JpaRepository<Speaker, Long> {
}
