package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.FamilyComposite;
import es.upm.miw.documents.FamilyType;
import es.upm.miw.dtos.ArticleFamilyMinimumDto;
import es.upm.miw.repositories.FamilyCompositeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestConfig
public class ArticlesFamilyControllerIT {

    @Autowired
    private ArticlesFamilyController articlesFamilyController;

    @Autowired
    private FamilyCompositeRepository familyCompositeRepository;

    @Test
    void testDeleteFamilyCompositeItem() {
        assertEquals("test", familyCompositeRepository.findByDescription("test").getDescription());
        articlesFamilyController.deleteFamilyCompositeItem(new ArticleFamilyMinimumDto(FamilyType.ARTICLES,"test"));
        assertNull(familyCompositeRepository.findByDescription("test"));
        familyCompositeRepository.save(new FamilyComposite(FamilyType.ARTICLES,"T","test"));
    }
}
