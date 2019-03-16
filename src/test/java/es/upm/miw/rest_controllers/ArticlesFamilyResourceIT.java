package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.ArticleFamilyMinimumDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
public class ArticlesFamilyResourceIT {

    @Autowired
    RestService restService;

    @Test
    void testReadAllFamilyCompositeByFamilyType() {
        List<ArticleFamilyMinimumDto> articleFamilyMinimumDtoList = Arrays.asList(this.restService.loginOperator()
                .restBuilder(new RestBuilder<ArticleFamilyMinimumDto[]>()).clazz(ArticleFamilyMinimumDto[].class)
                .path(ArticlesFamilyResource.ARTICLES_FAMILY)
                .param("familyType","ARTICLES").get().build());
        assertTrue(articleFamilyMinimumDtoList.size() > 1);
    }
}
