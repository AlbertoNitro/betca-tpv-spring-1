package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.ArticleLine;
import es.upm.miw.documents.Offer;
import es.upm.miw.dtos.output.ArticleSearchOutputDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class OfferRepositoryIT {

    @Autowired
    private OfferRepository offerRepository;

    private Offer offer;
    private ArticleLine articleLine_1;
    private ArticleLine articleLine_2;

    @BeforeEach
    void seedDb() {
        this.articleLine_1 = new ArticleLine();
        this.articleLine_1.setIdArticle("8400000000017");
        this.articleLine_1.setPercentage(5);
        this.articleLine_2 = new ArticleLine();
        this.articleLine_2.setIdArticle("8400000000024");
        this.articleLine_2.setPercentage(8);
        ArticleLine[] articleLines = { this.articleLine_1, this.articleLine_2 };

        this.offer = new Offer();
        this.offer.setId("0123456789");
        this.offer.setOffername("FakeOfferName");
        this.offer.setEndDate(LocalDateTime.now());
        this.offer.setArticleLine(articleLines);
        this.offerRepository.save(this.offer);
    }

    @AfterEach
    void delete() {
        this.offerRepository.delete(this.offer);
    }

    @Test
    void testReadAll() {
        assertTrue(this.offerRepository.findAll().size() > 1);
    }

    /*@Test
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
    void testFindFirstByCodeStartingWithOrderByRegistrationDateDescCodeDesc(){
        Article article = articleRepository.findFirstByCodeStartingWithOrderByRegistrationDateDescCodeDesc("84");
        assertEquals("8400000000085", article.getCode());
    }*/
}
