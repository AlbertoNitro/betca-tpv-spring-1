package es.upm.miw.repositories;

import es.upm.miw.documents.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfferRepository extends MongoRepository<Offer, String> {

}
