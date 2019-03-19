package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Article;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.ArticleSearchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class ArticleRepositoryIT {

    @Autowired
    private ArticleRepository articleRepository;

    private Article article;
    private Article article2;
    private Article article3;

    @BeforeEach
    void seedDb() {
        this.article = new Article();
        this.article.setCode("1");
        this.article.setDescription("art1");
        this.article.setRetailPrice(BigDecimal.valueOf(23.5));
        this.article.setStock(4);

        this.article2 = new Article();
        this.article2.setCode("2");
        this.article2.setDescription("Otro articulo");
        this.article2.setRetailPrice(BigDecimal.valueOf(22.6));
        this.article2.setStock(0);

        this.articleRepository.save(this.article);
        this.articleRepository.save(this.article2);
    }

    @Test
    void testReadAll() {
        assertTrue(this.articleRepository.findAll().size() > 4);
    }

    @Test
    void testFindByDescriptionLikeIgnoreCaseNullSafe(){
        List<ArticleSearchDto> articleList = articleRepository.findByDescriptionLikeIgnoreCaseNullSafe("Art");
        assertEquals(2, articleList.size());
        assertEquals("art1", articleList.get(0).getDescription());
        assertEquals("Otro articulo", articleList.get(1).getDescription());
    }

    @Test
    void testFindByDescriptionNull(){
        List<ArticleSearchDto> articleList = articleRepository.findByDescriptionLikeIgnoreCaseNullSafe(null);
        assertFalse(articleList.isEmpty());
    }

    @Test
    void testFindByStockGreaterThanEqual(){
        List<ArticleSearchDto> articleList = articleRepository.findByStockGreaterThanEqual(1);
        assertEquals(5, articleList.size());

        List<ArticleSearchDto> articleList2 = articleRepository.findByStockGreaterThanEqual(0);
        assertEquals(7, articleList2.size());

        List<ArticleSearchDto> articleList3 = articleRepository.findByStockGreaterThanEqual(10);
        assertEquals(1, articleList3.size());

        List<ArticleSearchDto> articleList4 = articleRepository.findByStockGreaterThanEqual(11);
        assertEquals(0, articleList4.size());
    }

    @Test
    void testFindByRetailPriceGreaterThanEqual(){
        List<ArticleSearchDto> articleList = articleRepository.findByRetailPriceGreaterThanEqual(BigDecimal.valueOf(0.24));
        assertEquals(6, articleList.size());

        List<ArticleSearchDto> articleList2 = articleRepository.findByRetailPriceGreaterThanEqual(BigDecimal.valueOf(0));
        assertEquals(7, articleList2.size());

        List<ArticleSearchDto> articleList3 = articleRepository.findByRetailPriceGreaterThanEqual(BigDecimal.valueOf(20));
        assertEquals(4, articleList3.size());
    }

    @Test
    void testFindByRetailPriceLessThanEqual(){
        List<ArticleSearchDto> articleList = articleRepository.findByRetailPriceLessThanEqual(BigDecimal.valueOf(0.24));
        assertEquals(1, articleList.size());

        List<ArticleSearchDto> articleList2 = articleRepository.findByRetailPriceLessThanEqual(BigDecimal.valueOf(0));
        assertEquals(0, articleList2.size());

        List<ArticleSearchDto> articleList3 = articleRepository.findByRetailPriceLessThanEqual(BigDecimal.valueOf(20));
        assertEquals(4, articleList3.size());
    }

    @Test
    void testFindByReferenceNullAndProviderNull(){
        List<ArticleSearchDto> articleList = articleRepository.findByReferenceNullAndProviderNull();
        assertEquals(2, articleList.size());
        assertEquals("art1", articleList.get(0).getDescription());
        assertEquals("Otro articulo", articleList.get(1).getDescription());
    }
}
