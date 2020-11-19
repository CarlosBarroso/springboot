package es.test.springboot.database.repositories;

import es.test.springboot.entities.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeakerRepository extends  JpaRepository<Speaker, Long> {
}
