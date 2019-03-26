package es.upm.miw.repositories;

import es.upm.miw.documents.TimeClock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeClockRepository extends MongoRepository<TimeClock, String> {
    List<TimeClock> findByClockinDateBetweenAndUserOrderByClockinDateDesc(LocalDateTime dateFrom, LocalDateTime dateTo, String id);

    TimeClock findFirst1ByUserOrderByClockinDateDesc(String id);
}
