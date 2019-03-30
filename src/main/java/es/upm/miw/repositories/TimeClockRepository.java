package es.upm.miw.repositories;

import es.upm.miw.documents.TimeClock;
import es.upm.miw.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeClockRepository extends MongoRepository<TimeClock, String> {

    Optional<TimeClock> findFirst1ByUserOrderByClockinDateDesc(String id);

    List<TimeClock> findByClockinDateBetweenOrderByClockinDateDesc(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<TimeClock> findByClockinDateBetweenAndUserOrderByClockinDateDesc(LocalDateTime dateFrom, LocalDateTime dateTo, String id);
}
