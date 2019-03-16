package es.upm.miw.rest_controllers;

import es.upm.miw.documents.FamilyComposite;
import es.upm.miw.documents.FamilyType;
import es.upm.miw.dtos.ArticleFamilyMinimumDto;
import es.upm.miw.repositories.FamilyCompositeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ApiTestConfig
public class ArticlesFamilyResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private FamilyCompositeRepository familyCompositeRepository;

    @Test
    void testReadAllFamilyCompositeByFamilyType() {
        List<ArticleFamilyMinimumDto> articleFamilyMinimumDtoList = Arrays.asList(this.restService.loginOperator()
                .restBuilder(new RestBuilder<ArticleFamilyMinimumDto[]>()).clazz(ArticleFamilyMinimumDto[].class)
                .path(ArticlesFamilyResource.ARTICLES_FAMILY)
                .param("familyType", "ARTICLES").get().build());
        assertTrue(articleFamilyMinimumDtoList.size() > 1);
    }

    @Test
    void testDeleteFamilyCompositeItem() {
        assertNotNull(familyCompositeRepository.findByDescription("test"));
        ArticleFamilyMinimumDto articleFamilyMinimumDto = this.restService.loginOperator()
                .restBuilder(new RestBuilder<ArticleFamilyMinimumDto>()).clazz(ArticleFamilyMinimumDto.class)
                .path(ArticlesFamilyResource.ARTICLES_FAMILY)
                .body(new ArticleFamilyMinimumDto(FamilyType.ARTICLES,"test")).delete().build();
        assertNull(familyCompositeRepository.findByDescription("test"));
        assertEquals("test",articleFamilyMinimumDto.getDescription());
        familyCompositeRepository.save(new FamilyComposite(FamilyType.ARTICLES, "T", "test"));
    }
}
