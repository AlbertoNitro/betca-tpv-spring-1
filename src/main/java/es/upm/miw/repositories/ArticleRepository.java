package es.upm.miw.repositories;

import es.upm.miw.documents.Article;
import es.upm.miw.dtos.ArticleSearchDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface ArticleRepository extends MongoRepository<Article, String> {

    @Query("{$and:["
            + "?#{ [0] == null ? { $where : 'true'} : { description : {$regex :[0], $options : 'i'} } },"
            + "?#{ [1] == null ? { $where : 'true'} : { stock : {$gte :[1]} } },"
            + "?#{ [2] == null ? { $where : 'true'} : { retailPrice : {$gte :[2]} } },"
            + "?#{ [3] == null ? { $where : 'true'} : { retailPrice : {$lte :[3]} } }"
            + "] }" )
    List<ArticleSearchDto> findByDescriptionAndStockAndRetailPriceNullSafe
            (String description, Integer stock, String minPrice, String maxPrice);

    Article findByCode(String code);

    List<ArticleSearchDto> findByReferenceNullAndProviderNull();

}
