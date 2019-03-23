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
    void testCreateFamilyArticle() {
        assertNotNull(familyCompositeRepository.findByDescription("test"));
        articlesFamilyController.createFamilyArticle(
                new ArticleMinimumDto("8400000000017", "Zarzuela - Falda T2"), "test");
        assertEquals(1, familyCompositeRepository.findByDescription("test").getArticlesFamilyList().size());
        FamilyComposite familyComposite = familyCompositeRepository.findByDescription("test");
        familyComposite.getFamilyCompositeList().clear();
        familyCompositeRepository.save(familyComposite);
        assertEquals(0, familyCompositeRepository.findByDescription("test").getArticlesFamilyList().size());
    }

    @Test
    void testCreateFamilyComposite() {
        assertNull(familyCompositeRepository.findByDescription("create"));
        articlesFamilyController.createFamilyComposite(
                new ArticleFamilyDto(FamilyType.ARTICLES, "C", "create"), "test");
        assertNotNull(familyCompositeRepository.findByDescription("create"));
        articlesFamilyController.createFamilyComposite(
                new ArticleFamilyDto(FamilyType.SIZES, null, "tS"), "test");
        assertNotNull(familyCompositeRepository.findByDescription("tS"));
        articlesFamilyController.deleteFamilyCompositeItem("create");
        articlesFamilyController.deleteFamilyCompositeItem("tS");
        assertNull(familyCompositeRepository.findByDescription("create"));
        assertThrows(BadRequestException.class, () -> articlesFamilyController.createFamilyComposite(
                new ArticleFamilyDto(FamilyType.ARTICLES, "C", "create"), "t"));
    }

    @Test
    void testDeleteComponentFromFamily() {
        List<ArticlesFamily> components = familyCompositeRepository.findByDescription("root").getArticlesFamilyList();
        assertNotNull(components);
        articlesFamilyController.deleteComponentFromFamily("root", "cards");
        assertEquals(components.size()-1,familyCompositeRepository.findByDescription("root").getArticlesFamilyList().size());
        assertNotNull(familyCompositeRepository.findByDescription("cards"));
        articlesFamilyController.deleteFamilyCompositeItem("cards");
        articlesFamilyController.createFamilyComposite(
                new ArticleFamilyDto(FamilyType.ARTICLES,"c","cards"),"root");
    }

    @Test
    void testDeleteFamilyCompositeItem() {
        assertEquals("test", familyCompositeRepository.findByDescription("test").getDescription());
        articlesFamilyController.deleteFamilyCompositeItem("test");
        assertNull(familyCompositeRepository.findByDescription("test"));
        familyCompositeRepository.save(new FamilyComposite(FamilyType.ARTICLES, "T", "test"));
    }

    @Test
    void testReadAllComponentsInAFamily() {
        assertNotNull(familyCompositeRepository.findByDescription("root"));
        assertNotNull(articlesFamilyController.readAllComponentsInAFamily("root"));
    }

}
