package es.upm.miw.repositories;

import es.upm.miw.documents.Alarm;
import es.upm.miw.dtos.AlarmDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends MongoRepository<Alarm, String> {

    //Optional<Alarm> findById(String id);

    @Query (value = "{}", fields = "{ '_id' : 0 }")
    List<AlarmDto> findAllAlarms();
    //TODO: perhaps is not necessary, cause teh "findAll()" method

    // TODO: find by Critical, Warning
}
