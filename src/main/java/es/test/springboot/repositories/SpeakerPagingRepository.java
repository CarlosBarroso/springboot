package es.test.springboot.repositories;

import es.test.springboot.models.Speaker;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SpeakerPagingRepository  extends PagingAndSortingRepository<Speaker, Integer> {
}
