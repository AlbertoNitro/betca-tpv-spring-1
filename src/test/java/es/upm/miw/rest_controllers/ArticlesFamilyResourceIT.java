package es.upm.miw.rest_controllers;

import es.upm.miw.documents.FamilyComposite;
import es.upm.miw.documents.FamilyType;
import es.upm.miw.dtos.ArticleFamilyDto;
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
    void testAttachToFamily() {
        assertNotNull(familyCompositeRepository.findFirstByDescription("test"));
        ArticleFamilyDto response = this.restService.loginOperator().restBuilder(new RestBuilder<ArticleFamilyDto>())
                .clazz(ArticleFamilyDto.class).path(ArticlesFamilyResource.ARTICLES_FAMILY).path(ArticlesFamilyResource.DESCRIPTION)
                .expand("test").body(new ArticleFamilyDto(FamilyType.ARTICLES, "B", "Books")).post().build();
        assertNotNull(response);
        assertEquals("Books", response.getDescription());
    }

    @Test
    void testCreateFamilyComposite() {
        assertNull(familyCompositeRepository.findFirstByDescription("create"));
        assertNotNull(familyCompositeRepository.findFirstByDescription("test"));
        ArticleFamilyDto response = this.restService.loginOperator().restBuilder(new RestBuilder<ArticleFamilyDto>())
                .clazz(ArticleFamilyDto.class).path(ArticlesFamilyResource.ARTICLES_FAMILY + ArticlesFamilyResource.CREATE)
                .param("description", "test").body(new ArticleFamilyDto(FamilyType.ARTICLES, "C", "create"))
                .post().build();
        assertEquals("create", response.getDescription());
        assertNotNull(familyCompositeRepository.findFirstByDescription("create"));
        familyCompositeRepository.delete(familyCompositeRepository.findFirstByDescription("create"));
    }

    @Test
    void testDeleteComponentFromFamily() {
        assertDoesNotThrow(() -> this.restService.loginOperator().restBuilder()
                .path(ArticlesFamilyResource.ARTICLES_FAMILY).path(ArticlesFamilyResource.DESCRIPTION)
                .expand("root").param("childDescription", "cards").delete().build());
    }

    @Test
    void testDeleteFamilyCompositeItem() {
        assertNotNull(familyCompositeRepository.findFirstByDescription("test"));
        this.restService.loginOperator().restBuilder().path(ArticlesFamilyResource.ARTICLES_FAMILY)
                .param("description", "test").delete().build();
        assertNull(familyCompositeRepository.findFirstByDescription("test"));
        familyCompositeRepository.save(new FamilyComposite(FamilyType.ARTICLES, "T", "test"));
    }

    @Test
    void testReadAllComponentsInAFamily() {
        List<ArticleFamilyDto> dtos = Arrays.asList(this.restService.loginOperator()
                .restBuilder(new RestBuilder<ArticleFamilyDto[]>()).clazz(ArticleFamilyDto[].class)
                .path(ArticlesFamilyResource.ARTICLES_FAMILY)
                .path(ArticlesFamilyResource.DESCRIPTION).expand("root").get().build());
        assertNotNull(dtos);
    }

    @Test
    void testReadAllFamilyCompositeByFamilyType() {
        List<ArticleFamilyMinimumDto> articleFamilyMinimumDtoList = Arrays.asList(this.restService.loginOperator()
                .restBuilder(new RestBuilder<ArticleFamilyMinimumDto[]>()).clazz(ArticleFamilyMinimumDto[].class)
                .path(ArticlesFamilyResource.ARTICLES_FAMILY)
                .param("familyType", "ARTICLES").get().build());
        assertTrue(articleFamilyMinimumDtoList.size() > 1);
    }
}
