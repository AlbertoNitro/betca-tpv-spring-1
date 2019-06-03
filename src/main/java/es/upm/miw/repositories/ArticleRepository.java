package es.upm.miw.repositories;

import es.upm.miw.documents.Article;
import es.upm.miw.documents.Provider;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.output.ArticleSearchOutputDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends MongoRepository<Article, String> {

    @Query("{$and:["
            + "?#{ [0] == null ? { $where : 'true'} : { description : {$regex :[0], $options : 'i'} } },"
            + "?#{ [1] == null ? { $where : 'true'} : { stock : {$gte :[1]} } },"
            + "?#{ [2] == null ? { $where : 'true'} : { retailPrice : {$gte :[2]} } },"
            + "?#{ [3] == null ? { $where : 'true'} : { retailPrice : {$lte :[3]} } }"
            + "] }")
    List<ArticleSearchOutputDto> findByDescriptionAndStockAndRetailPriceNullSafe
            (String description, Integer stock, String minPrice, String maxPrice);

    Article findByCode(String code);

    List<ArticleSearchOutputDto> findByReferenceNullAndProviderNull();

    Article findFirstByCodeStartingWithOrderByRegistrationDateDescCodeDesc(String prefix);

    //@Query(value = "{'provider.id':?0}", fields = "{'_id':0,'code':1}")
    //List<Article> findByProvider(String id);

    //@Query(value = "{}", fields = "{'_id':0,'code':1}")
    List<ArticleSearchOutputDto> findAllByProvider(Optional<Provider> provider);

    List<Article> findByStockBetween(Integer stockMin, Integer stockLimit);

    List<Article> findByStockLessThan(Integer stockMin);

}
