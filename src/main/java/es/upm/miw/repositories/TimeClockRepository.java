package es.upm.miw.repositories;

import es.upm.miw.documents.TimeClock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimeClockRepository extends MongoRepository<TimeClock, String> {
}
