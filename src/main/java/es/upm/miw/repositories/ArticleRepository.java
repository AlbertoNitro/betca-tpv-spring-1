package es.upm.miw.repositories;

import es.upm.miw.documents.Article;
import es.upm.miw.dtos.ArticleSearchDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ArticleRepository extends MongoRepository<Article, String> {

    @Query("?#{ [0] == null ? { $where : 'true'} : { description : {$regex:[0], $options: 'i'} } }")
    List<ArticleSearchDto> findByDescriptionLikeIgnoreCaseNullSafe(String description);

    List<ArticleSearchDto> findByStockGreaterThanEqual(int stock);

    List<ArticleSearchDto> findByRetailPriceGreaterThanEqual(BigDecimal minPrice);

    List<ArticleSearchDto> findByRetailPriceLessThanEqual(BigDecimal maxPrice);

}
