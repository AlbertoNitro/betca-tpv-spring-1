package es.upm.miw.repositories;

import es.upm.miw.documents.Offer;
import es.upm.miw.dtos.output.OfferOutputDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface OfferRepository extends MongoRepository<Offer, String> {

    @Query("{ $and:["
            + "?#{ [0] == null ? { $where : 'true'} : { id : {$regex:[0], $options: 'i'} } },"
            + "?#{ [1] == null ? { $where : 'true'} : { offername : {$regex:[1], $options: 'i'} } },"
            + "?#{ [2] == 'false' ? { $where : 'true'} : { endDate : { $gte: [3] } } },"
            + "?#{ [4] == null ? { $where : 'true'} : { 'articleLine._id' : {$regex:[4], $options: 'i'} } }"
            + "] }")
    List<OfferOutputDto> findByIdOffernameEndDateArticleId(String id, String offername, String status, Date now, String articleLine);
}

