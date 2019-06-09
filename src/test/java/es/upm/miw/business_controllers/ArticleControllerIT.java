package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.*;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.input.FamilySizeInputDto;
import es.upm.miw.dtos.output.ArticleSearchOutputDto;
import es.upm.miw.exceptions.ConflictException;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.FamilyCompositeRepository;
import es.upm.miw.repositories.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    private ProviderRepository providerRepository;

    @Autowired
    private FamilyCompositeRepository familyCompositeRepository;

    @BeforeEach
    void seed() {
        this.articleDto = new ArticleDto("non exist", "descrip", "ref", BigDecimal.TEN, null, Tax.SUPER_REDUCED);
        this.article = new Article();
        this.article.setCode("99999999");
        this.articleRepository.save(this.article);
    }

    @Test
    void testReadAllArticles() {
        List<ArticleSearchOutputDto> articles = this.articleController.readAll();
        assertNotNull(articles);
        assertTrue(articles.size() > 0);
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
    void findArticleByProvider(){
        List<ArticleSearchOutputDto> articles = articleController.findArticleByProvider("5c9fe57b8f8e3f5f344caf8f");
        assertTrue(articles.size() > 0);
    }

    @Test
    void testUpdateArticleNotFoundException() {
        assertThrows(NotFoundException.class, () -> this.articleController.update("miw", articleDto));
    }

    @Test
    void testUpdateArticleWithProviderNotFoundException() {
        this.articleDto.setProvider("non exist");
        assertThrows(NotFoundException.class, () -> this.articleController.update(this.article.getCode(), articleDto));
    }

    @Test
    void testUpdateArticle() {
        this.articleDto.setDescription("miw");
        ArticleDto article = this.articleController.update(this.article.getCode(), articleDto);

        assertNotNull(article);
        assertEquals("miw", article.getDescription());
    }

    @Test
    void testDeleteArticleNotExist() {
        List<ArticleSearchOutputDto> articleBeforeDelete = this.articleController.readAll();
        this.articleController.delete("miw");
        List<ArticleSearchOutputDto> articleAfterDelete = this.articleController.readAll();
        assertEquals(articleBeforeDelete.size(), articleAfterDelete.size());
    }

    @Test
    void testDeleteArticleExist() {
        List<ArticleSearchOutputDto> articleBeforeDelete = this.articleController.readAll();
        this.articleController.delete(this.article.getCode());
        List<ArticleSearchOutputDto> articleAfterDelete = this.articleController.readAll();
        assertTrue(articleBeforeDelete.size() > articleAfterDelete.size());
    }

    @Test
    void testReadArticlesMinimumStock(){
        List<ArticleDto> articles = this.articleController.readArticlesMinimumStock(10);
        assertNotNull(articles);
        assertTrue(articles.size() > 0);
    }

    @Test
    void testReadArticlesReservation(){
        List<ArticleDto> articles = this.articleController.readArticlesReservation();
        assertNotNull(articles);
        assertTrue(articles.size() > 0);
    }

    @Test
    Provider testGetProvider() {
        Provider provider = this.articleController.getProvider("Zara");
        String id = provider.getId();
        Provider pvd = this.articleController.getProvider("Zara");
        assertEquals(id, pvd.getId());
        return provider;
    }

    @Test
    Article testCreateArticleForSingleSize() {
        Article article = this.articleController.createArticleForEachSize("M", "R001", "T-Shirt", this.testGetProvider());
        Article art = this.articleRepository.findByCode(article.getCode());
        assertEquals("T-Shirt T-M", art.getDescription());
        return art;
    }

    @Test
    void testCreateSizeArticleFamily(){
        FamilyArticle familyArticle = this.articleController.createFamilyArticle(this.testCreateArticleForSingleSize());
        assertEquals("T-Shirt T-M", familyArticle.getDescription());
        assertEquals(FamilyType.ARTICLE, familyArticle.getFamilyType());
    }

    @Test
    FamilyComposite testCreateSizeFamilyComposite(){
        ArrayList<String> sizeArray = new ArrayList<>();
        for(int i = 34; i < 42; i = i+2) {
            sizeArray.add(Integer.toString(i));
        }
        FamilyComposite familyComposite = this.articleController.createFamilyComposite("Ref.01", "Short", this.testGetProvider(), sizeArray);
        assertEquals("Short", familyComposite.getDescription());
        assertEquals(FamilyType.SIZES, familyComposite.getFamilyType());
        assertEquals(4, familyComposite.getArticlesFamilyList().size());
        return familyComposite;
    }

    @Test
    void testCreateFamilySize(){
        ArrayList<String> sizeArray = new ArrayList<>();
        for(int i = 34; i < 42; i = i+2) {
            sizeArray.add(Integer.toString(i));
        }
        FamilySizeInputDto familySizeInputDto = new FamilySizeInputDto("Ref","Short", "Zara", sizeArray);
        FamilySizeInputDto responseFSIO = this.articleController.createFamilySize(familySizeInputDto);
        assertEquals("Ref", responseFSIO.getReference());
    }
}
