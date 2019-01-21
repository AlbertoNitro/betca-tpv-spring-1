package es.upm.miw.repositories;

import es.upm.miw.documents.Property;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PropertyRepository extends MongoRepository<Property, String> {

}
