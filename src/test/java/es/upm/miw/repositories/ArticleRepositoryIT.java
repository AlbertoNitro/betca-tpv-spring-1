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

}
