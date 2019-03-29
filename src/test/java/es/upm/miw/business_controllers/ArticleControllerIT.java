package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Tax;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.output.ArticleSearchOutputDto;
import es.upm.miw.exceptions.ConflictException;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.ArticleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class ArticleControllerIT {

    @Autowired
    private ArticleController articleController;

    @Autowired
    private ArticleRepository articleRepository;

    private ArticleDto articleDto;
    private Article article;

    @BeforeEach
    void seed() {
        this.articleDto = new ArticleDto("non exist", "descrip", "ref", BigDecimal.TEN, null, Tax.SUPER_REDUCED);
        this.article = new Article();
        this.article.setCode("99999999");
        this.articleRepository.save(this.article);
    }

    @Test
    void testReadArticles() {
        List<ArticleSearchOutputDto> articles = articleController.readArticles("d", null, null, null);
        assertTrue(articles.size() > 0);

        List<ArticleSearchOutputDto> articles2 = articleController.readArticles(null, null, BigDecimal.ZERO, BigDecimal.TEN);
        assertTrue(articles2.size() > 0);
    }

    @Test
    void testReadArticlesPartiallyDefined() {
        List<ArticleSearchOutputDto> articles = articleController.readArticles();
        assertTrue(articles.size() > 0);
    }

    @Test
    void testConflictRequestException() {
        this.articleDto.setCode("8400000000017");
        assertThrows(ConflictException.class, () -> this.articleController.createArticle(this.articleDto));
    }

    @Test
    void testProviderNotFoundException() {
        this.articleDto.setProvider("non exist");
        assertThrows(NotFoundException.class, () -> this.articleController.createArticle(articleDto));
    }

    @Test
    void testInitStock() {
        assertNotNull(this.articleController.createArticle(articleDto).getStock());
        this.articleRepository.deleteById(this.articleDto.getCode());
    }

    @Test
    void testUpdateArticleNotFoundException() {
        assertThrows(NotFoundException.class, () -> this.articleController.update("miw", articleDto));
    }

    @AfterEach
    void delete() {
        this.articleRepository.delete(article);
    }

}
