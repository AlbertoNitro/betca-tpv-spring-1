package es.upm.miw.restControllers;

import es.upm.miw.dtos.ArticleDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
class ArticleResorceIT {

    @Autowired
    private RestService restService;

    @Test
    void testReadArticleOne() {
        ArticleDto articleDto = this.restService.loginAdmin().restBuilder(new RestBuilder<ArticleDto>()).clazz(ArticleDto.class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).expand("1")
                .get().build();
        assertNotNull(articleDto);
    }

    @Test
    void testReadArticleNonExist() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder()
                        .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).expand("kk")
                        .get().build());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

}
