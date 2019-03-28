package es.upm.miw.repositories;

import es.upm.miw.documents.RgpdAgreement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RgpdAgreementRepository extends MongoRepository<RgpdAgreement, String> {

    List<RgpdAgreement> findByAssignee(String userId);
}
