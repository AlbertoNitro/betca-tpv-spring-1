package es.upm.miw.rest_controllers;

import es.upm.miw.documents.Article;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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

    @Test
    void testReadArticlesMinimum() {
        List<ArticleMinimumDto> dtos = Arrays.asList(this.restService.loginAdmin().restBuilder(new RestBuilder<ArticleMinimumDto[]>())
                .clazz(ArticleMinimumDto[].class).path(ArticleResource.ARTICLES).path(ArticleResource.MINIMUM)
                .get().build());
        assertTrue(dtos.size() > 1);
    }

    @Test
    void testCreateArticleRepeated() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder()
                        .path(ArticleResource.ARTICLES)
                        .body(new ArticleDto("8400000000017", "repeated", "", BigDecimal.TEN, 10))
                        .post().build());
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void testCreateArticleNegativePrice() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder()
                        .path(ArticleResource.ARTICLES)
                        .body(new ArticleDto("4800000000011", "new", "", new BigDecimal("-1"), 10))
                        .post().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testCreateArticleWithoutCodeNextCodeEanNotImplemented() {
        HttpClientErrorException.BadRequest exception = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                this.restService.loginAdmin().restBuilder()
                        .path(ArticleResource.ARTICLES)
                        .body(new ArticleDto(null, "new", "", BigDecimal.TEN, 10))
                        .post().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
