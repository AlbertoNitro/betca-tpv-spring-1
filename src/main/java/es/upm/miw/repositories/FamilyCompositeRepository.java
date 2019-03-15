package es.upm.miw.repositories;

import es.upm.miw.documents.FamilyComposite;
import es.upm.miw.documents.FamilyType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FamilyCompositeRepository extends MongoRepository<FamilyComposite, String> {
    List<FamilyComposite> findAllFamilyCompositeByFamilyType(FamilyType familyType);
    FamilyComposite findByDescription(String description);
}
