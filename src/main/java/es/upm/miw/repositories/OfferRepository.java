package es.upm.miw.repositories;

import es.upm.miw.documents.Offer;
import es.upm.miw.dtos.output.OfferOutputDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OfferRepository extends MongoRepository<Offer, String> {

    @Query("{ $and:["
            + "?#{ [0] == null ? { $where : 'true'} : { id : {$regex:[0], $options: 'i'} } },"
            + "?#{ [1] == null ? { $where : 'true'} : { offername : {$regex:[1], $options: 'i'} } },"
            + "?#{ [2] == 'false' ? { $where : 'true'} : { endDate : { $gte: new Date() } } }"
            + "] }")
    List<OfferOutputDto> findByIdOffername(String id, String offername, String status);
}

