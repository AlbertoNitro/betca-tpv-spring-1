package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Provider;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.output.ArticleSearchOutputDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class ArticleRepositoryIT {

    @Autowired
    private ArticleRepository articleRepository;

    private Article article;
    private Article article2;

    @Autowired
    private ProviderRepository providerRepository;

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
    void testFindByReferenceNullAndProviderNull() {
        List<ArticleSearchOutputDto> articleList = articleRepository.findByReferenceNullAndProviderNull();
        assertEquals(4, articleList.size());
    }

    @Test
    void testFindByCode() {
        assertNotNull(this.articleRepository.findByCode("8400000000031"));
        assertEquals("descrip-a3", this.articleRepository.findByCode("8400000000031").getDescription());
    }

    @Test
    void testFindByDescriptionAndStockAndRetailPriceNull(){
        List<ArticleSearchOutputDto> articleList = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                (null, null, null, null);
        assertFalse(articleList.isEmpty());
    }

    @Test
    void testFindByDescription(){
        List<ArticleSearchOutputDto> articleList = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                ("zaR", null, null, null);
        assertEquals(2, articleList.size());
    }

    @Test
    void testFindByStock(){
        List<ArticleSearchOutputDto> articleList = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                (null, 2, null, null);
        assertEquals(6, articleList.size());
    }

    @Test
    void testFindByRetailPrice(){
        List<ArticleSearchOutputDto> articleList = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                (null, null, "22.6", null);
        assertEquals(3, articleList.size());
    }

    @Test
    void testFindByDescriptionAndStock(){
        List<ArticleSearchOutputDto> articleList = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                ("zAr", 2, null, null);
        assertEquals(2, articleList.size());
    }

    @Test
    void testFindByStockAndRetailPrice(){
        List<ArticleSearchOutputDto> articleList = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                (null, 2, "20", null);
        assertEquals(5, articleList.size());
    }

    @Test
    void testFindByDescriptionAndStockAndRetailPrice() {
        List<ArticleSearchOutputDto> articleList = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                ("zaR", 7, "2", "21");
        assertEquals(1, articleList.size());
    }

    @Test
    void testFindArticleByProvider() {
        Optional<Provider> provider = providerRepository.findById("company-p1");
        List<ArticleSearchOutputDto> articles=articleRepository.findAllByProvider(provider);
        assertTrue(articles.size() >= 0);
    }



    @Test
    void testFindFirstByCodeStartingWithOrderByRegistrationDateDescCodeDesc(){
        Article article = articleRepository.findFirstByCodeStartingWithOrderByRegistrationDateDescCodeDesc("84");
        assertEquals("8400000000085", article.getCode());
    }
}
