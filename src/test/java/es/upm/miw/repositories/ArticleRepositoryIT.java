package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Article;
import es.upm.miw.dtos.output.ArticleSearchOutputDto;
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
        assertEquals(2, articleList.size());
        assertEquals("art1", articleList.get(0).getDescription());
        assertEquals("Otro articulo", articleList.get(1).getDescription());
    }

    @Test
    void testFindByCode() {
        assertNotNull(this.articleRepository.findByCode("1"));
        assertEquals("art1", this.articleRepository.findByCode("1").getDescription());
    }

    @Test
    void testFindByDescriptionAndStockAndRetailPriceNullSafe() {
        List<ArticleSearchOutputDto> articleList = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                (null, null, null, null);
        assertFalse(articleList.isEmpty());

        List<ArticleSearchOutputDto> articleList2 = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                ("Art", null, null, null);
        assertEquals(2, articleList2.size());

        List<ArticleSearchOutputDto> articleList3 = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                (null, 2, null, null);
        assertEquals(4, articleList3.size());

        List<ArticleSearchOutputDto> articleList4 = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                (null, null, "22.6", null);
        assertEquals(3, articleList4.size());

        List<ArticleSearchOutputDto> articleList5 = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                ("Art", 2, null, null);
        assertEquals(1, articleList5.size());

        List<ArticleSearchOutputDto> articleList6 = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                ("Art", null, null, "23");
        assertEquals(1, articleList6.size());

        List<ArticleSearchOutputDto> articleList7 = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                (null, 2, "20", null);
        assertEquals(3, articleList7.size());

        List<ArticleSearchOutputDto> articleList8 = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                (null, null, "20", "27");
        assertEquals(3, articleList8.size());

        List<ArticleSearchOutputDto> articleList9 = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                ("Art", null, "20", "23");
        assertEquals(1, articleList9.size());

        List<ArticleSearchOutputDto> articleList10 = articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                ("zaR", 7, "2", "21");
        assertEquals(1, articleList10.size());
    }
}
