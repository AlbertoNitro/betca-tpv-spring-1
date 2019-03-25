package es.upm.miw.rest_controllers;

import es.upm.miw.documents.Tax;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import es.upm.miw.dtos.input.ArticleSearchInputDto;
import es.upm.miw.dtos.output.ArticleSearchOutputDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

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
    void testReallAll(){
        List<ArticleSearchOutputDto> articles = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<ArticleSearchOutputDto[]>()).clazz(ArticleSearchOutputDto[].class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.SEARCH).body(new ArticleSearchInputDto(null, null, null, null))
                .post().build());
        assertTrue(articles.size() > 0);
    }

    @Test
    void testReallArticlesBy1Field(){
        List<ArticleSearchOutputDto> articles = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<ArticleSearchOutputDto[]>()).clazz(ArticleSearchOutputDto[].class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.SEARCH).body(new ArticleSearchInputDto("a", null, null, null))
                .post().build());
        assertTrue(articles.size() > 0);
    }

    @Test
    void testReallPartiallyDefined(){
        List<ArticleSearchOutputDto> articles = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<ArticleSearchOutputDto[]>()).clazz(ArticleSearchOutputDto[].class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.SEARCH).path(ArticleResource.PARTIALLY_DEFINED)
                .post().build());
        assertTrue(articles.size() == 0);
    }

    @Test
    void testCreateArticleRepeated() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder()
                        .path(ArticleResource.ARTICLES)
                        .body(new ArticleDto("8400000000017", "repeated", "", BigDecimal.TEN, 10, Tax.GENERAL))
                        .post().build());
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void testCreateArticleNegativePrice() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder()
                        .path(ArticleResource.ARTICLES)
                        .body(new ArticleDto("4800000000011", "new", "", new BigDecimal("-1"), 10, Tax.REDUCED))
                        .post().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testCreateArticleWithoutCodeNextCodeEanNotImplemented() {
        HttpClientErrorException.BadRequest exception = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                this.restService.loginAdmin().restBuilder()
                        .path(ArticleResource.ARTICLES)
                        .body(new ArticleDto(null, "new", "", BigDecimal.TEN, 10, Tax.FREE))
                        .post().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
