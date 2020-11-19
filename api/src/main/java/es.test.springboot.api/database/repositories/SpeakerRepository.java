package es.test.springboot.api.database.repositories;

import es.test.springboot.api.database.entities.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeakerRepository extends  JpaRepository<Speaker, Long> {
}
