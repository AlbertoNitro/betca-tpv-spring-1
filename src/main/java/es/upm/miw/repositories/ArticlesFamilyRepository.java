package es.upm.miw.repositories;

import es.upm.miw.documents.ArticlesFamily;
import es.upm.miw.documents.FamilyType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticlesFamilyRepository extends MongoRepository<ArticlesFamily, String> {

}
