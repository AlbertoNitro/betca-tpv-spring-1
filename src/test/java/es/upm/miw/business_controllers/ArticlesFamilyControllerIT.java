package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.ArticlesFamily;
import es.upm.miw.documents.FamilyComposite;
import es.upm.miw.documents.FamilyType;
import es.upm.miw.dtos.ArticleFamilyDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.repositories.FamilyCompositeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class ArticlesFamilyControllerIT {

    @Autowired
    private ArticlesFamilyController articlesFamilyController;

    @Autowired
    private FamilyCompositeRepository familyCompositeRepository;

    @Test
    void testAttachToFamilyArticle (){
        assertNotNull(familyCompositeRepository.findFirstByDescription("test"));
        articlesFamilyController.attachToFamily(
                new ArticleFamilyDto(FamilyType.ARTICLE,"8400000000017", "Zarzuela - Falda T2"), "test");
        assertEquals(1, familyCompositeRepository.findFirstByDescription("test").getArticlesFamilyList().size());
        FamilyComposite familyComposite = familyCompositeRepository.findFirstByDescription("test");
        familyComposite.getFamilyCompositeList().clear();
        familyCompositeRepository.save(familyComposite);
        assertEquals(0, familyCompositeRepository.findFirstByDescription("test").getArticlesFamilyList().size());
    }

    @Test
    void testAttachToFamilyFamilyComposite (){
        assertNotNull(familyCompositeRepository.findFirstByDescription("test"));
        articlesFamilyController.attachToFamily(
                new ArticleFamilyDto(FamilyType.ARTICLES,"B", "Books"), "test");
        assertEquals(1, familyCompositeRepository.findFirstByDescription("test").getArticlesFamilyList().size());
        FamilyComposite familyComposite = familyCompositeRepository.findFirstByDescription("test");
        familyComposite.getFamilyCompositeList().clear();
        familyCompositeRepository.save(familyComposite);
        assertEquals(0, familyCompositeRepository.findFirstByDescription("test").getArticlesFamilyList().size());
    }

    @Test
    void testCreateFamilyArticle() {
        assertNotNull(familyCompositeRepository.findFirstByDescription("test"));
        articlesFamilyController.createFamilyArticle(
                new ArticleMinimumDto("8400000000017", "Zarzuela - Falda T2"), "test");
        assertEquals(1, familyCompositeRepository.findFirstByDescription("test").getArticlesFamilyList().size());
        FamilyComposite familyComposite = familyCompositeRepository.findFirstByDescription("test");
        familyComposite.getFamilyCompositeList().clear();
        familyCompositeRepository.save(familyComposite);
        assertEquals(0, familyCompositeRepository.findFirstByDescription("test").getArticlesFamilyList().size());
    }

    @Test
    void testCreateFamilyComposite() {
        assertNull(familyCompositeRepository.findFirstByDescription("create"));
        articlesFamilyController.createArticleFamily(
                new ArticleFamilyDto(FamilyType.ARTICLES, "C", "create"), "test");
        assertNotNull(familyCompositeRepository.findFirstByDescription("create"));
        articlesFamilyController.createArticleFamily(
                new ArticleFamilyDto(FamilyType.SIZES, null, "tS"), "test");
        assertNotNull(familyCompositeRepository.findFirstByDescription("tS"));
        articlesFamilyController.deleteFamilyCompositeItem("create");
        articlesFamilyController.deleteFamilyCompositeItem("tS");
        assertNull(familyCompositeRepository.findFirstByDescription("create"));
    }

    @Test
    void testDeleteComponentFromFamily() {
        List<ArticlesFamily> components = familyCompositeRepository.findFirstByDescription("root").getArticlesFamilyList();
        assertNotNull(components);
        articlesFamilyController.deleteComponentFromFamily("root", "cards");
        assertEquals(components.size()-1,familyCompositeRepository.findFirstByDescription("root").getArticlesFamilyList().size());
        assertNotNull(familyCompositeRepository.findFirstByDescription("cards"));
        articlesFamilyController.deleteFamilyCompositeItem("cards");
        articlesFamilyController.createArticleFamily(
                new ArticleFamilyDto(FamilyType.ARTICLES,"c","cards"),"root");
    }

    @Test
    void testDeleteFamilyCompositeItem() {
        assertEquals("test", familyCompositeRepository.findFirstByDescription("test").getDescription());
        articlesFamilyController.deleteFamilyCompositeItem("test");
        assertNull(familyCompositeRepository.findFirstByDescription("test"));
        familyCompositeRepository.save(new FamilyComposite(FamilyType.ARTICLES, "T", "test"));
    }

    @Test
    void testExistFamily(){
        assertThrows(BadRequestException.class, () -> articlesFamilyController.createArticleFamily(
                new ArticleFamilyDto(FamilyType.ARTICLES, "C", "create"), "t"));
    }

    @Test
    void testReadAllComponentsInAFamily() {
        assertNotNull(familyCompositeRepository.findFirstByDescription("root"));
        assertNotNull(articlesFamilyController.readAllComponentsInAFamily("root"));
    }

}
