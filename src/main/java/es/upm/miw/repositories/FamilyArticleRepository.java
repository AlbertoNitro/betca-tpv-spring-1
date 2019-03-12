package es.upm.miw.repositories;

import es.upm.miw.documents.FamilyArticle;
import es.upm.miw.documents.FamilyType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FamilyArticleRepository extends MongoRepository<FamilyArticle, String> {

    List<FamilyArticle> findByFamilyType (FamilyType familyType);

}
