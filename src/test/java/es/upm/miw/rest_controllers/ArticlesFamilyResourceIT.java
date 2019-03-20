package es.upm.miw.rest_controllers;

import es.upm.miw.documents.FamilyComposite;
import es.upm.miw.documents.FamilyType;
import es.upm.miw.dtos.ArticleFamilyMinimumDto;
import es.upm.miw.dtos.FamilyCompositeDto;
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
        this.restService.loginOperator()
                .restBuilder(new RestBuilder<ArticleFamilyMinimumDto>()).clazz(ArticleFamilyMinimumDto.class)
                .path(ArticlesFamilyResource.ARTICLES_FAMILY)
                .param("description", "test").delete().build();
        assertNull(familyCompositeRepository.findByDescription("test"));
        familyCompositeRepository.save(new FamilyComposite(FamilyType.ARTICLES, "T", "test"));
    }

    @Test
    void testCreateFamilyComposite() {
        assertNull(familyCompositeRepository.findByDescription("create"));
        assertNotNull(familyCompositeRepository.findByDescription("test"));
        FamilyCompositeDto response = this.restService.loginOperator().restBuilder(new RestBuilder<FamilyCompositeDto>())
                .clazz(FamilyCompositeDto.class).path(ArticlesFamilyResource.ARTICLES_FAMILY + ArticlesFamilyResource.COMPOSITE)
                .param("description", "test").body(new FamilyCompositeDto(FamilyType.ARTICLES, "C", "create"))
                .post().build();
        assertEquals("create", response.getDescription());
        assertNotNull(familyCompositeRepository.findByDescription("create"));
        familyCompositeRepository.delete(familyCompositeRepository.findByDescription("create"));
    }
}
