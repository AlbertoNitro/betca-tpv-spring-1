package es.upm.miw.rest_controllers;

import es.upm.miw.documents.Article;
import es.upm.miw.documents.Tax;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import es.upm.miw.dtos.input.ArticleSearchInputDto;
import es.upm.miw.dtos.input.FamilySizeInputDto;
import es.upm.miw.dtos.output.ArticleSearchOutputDto;
import es.upm.miw.repositories.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
class ArticleResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private ArticleRepository articleRepository;

    private ArticleDto articleDto;
    private Article article;

    @BeforeEach
    void seed() {
        this.articleDto = new ArticleDto("miw-dto", "descrip", "ref", BigDecimal.TEN, null, Tax.SUPER_REDUCED);
        this.article = new Article();
        this.article.setCode("99999999");
        this.articleRepository.save(this.article);
    }

    @Test
    void testReadAllArticles() {
        List<ArticleSearchOutputDto> articles = readAllArticles();

        assertNotNull(articles);
        assertTrue(articles.size() > 0);
    }

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
    void testArticlesMinimumStock() {
        List<ArticleDto> dtos = Arrays.asList(this.restService.loginAdmin().restBuilder(new RestBuilder<ArticleDto[]>())
                .clazz(ArticleDto[].class).path(ArticleResource.ARTICLES).path(ArticleResource.MINIMUM_STOCK).expand("6")
                .get().build());
        assertNotNull(dtos);
        assertTrue(dtos.size() > 1);
    }

    @Test
    void testArticlesReservation() {
        List<ArticleDto> dtos = Arrays.asList(this.restService.loginAdmin().restBuilder(new RestBuilder<ArticleDto[]>())
                .clazz(ArticleDto[].class).path(ArticleResource.ARTICLES).path(ArticleResource.RESERVATION)
                .get().build());
        assertNotNull(dtos);
        assertTrue(dtos.size() > 0);
    }

    @Test
    void testReadAll() {
        List<ArticleSearchOutputDto> articles = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<ArticleSearchOutputDto[]>()).clazz(ArticleSearchOutputDto[].class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.SEARCH).body(new ArticleSearchInputDto(null, null, null, null))
                .post().build());
        assertTrue(articles.size() > 0);
    }

    @Test
    void testReadArticlesBy1Field() {
        List<ArticleSearchOutputDto> articles = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<ArticleSearchOutputDto[]>()).clazz(ArticleSearchOutputDto[].class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.SEARCH).body(new ArticleSearchInputDto("a", null, null, null))
                .post().build());
        assertTrue(articles.size() > 0);
    }

    @Test
    void testReadPartiallyDefined() {
        List<ArticleSearchOutputDto> articles = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<ArticleSearchOutputDto[]>()).clazz(ArticleSearchOutputDto[].class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.SEARCH).path(ArticleResource.PARTIALLY_DEFINED)
                .post().build());
        assertTrue(articles.size() > 0);
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
    void testCreateArticleWithoutCodeNextCodeEan() {
        ArticleDto articleInputDto = new ArticleDto(null, "new", "", BigDecimal.TEN, 10, Tax.FREE);

        ArticleDto articleOutputDto = this.restService.loginAdmin().restBuilder(new RestBuilder<ArticleDto>()).clazz(ArticleDto.class)
                .path(ArticleResource.ARTICLES)
                .body(articleInputDto)
                .post().build();

        assertNotNull(articleOutputDto);
        assertNotNull(articleOutputDto.getCode());
    }

    @Test
    void testUpdateArticleNotExist() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder(new RestBuilder<ArticleDto>()).clazz(ArticleDto.class)
                        .path(ArticleResource.ARTICLES).path("/miw").body(this.articleDto).put().build());

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testUpdateArticleExist() {
        this.articleDto.setDescription("miw");
        ArticleDto articleOutputDto = this.restService.loginAdmin().restBuilder(new RestBuilder<ArticleDto>()).clazz(ArticleDto.class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).expand("99999999").body(this.articleDto).put().build();

        assertEquals(this.articleDto.getDescription(), articleOutputDto.getDescription());
    }

    @Test
    void testDeleteArticleExist() {
        List<ArticleSearchOutputDto> articlesBeforeDelete = readAllArticles();

        this.restService.loginAdmin().restBuilder(new RestBuilder<ArticleDto>()).clazz(ArticleDto.class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).expand("99999999").delete().build();

        List<ArticleSearchOutputDto> articlesAfterDelete = readAllArticles();

        assertTrue(articlesBeforeDelete.size() > articlesAfterDelete.size());

    }

    @Test
    void testDeleteArticleNotExist() {
        List<ArticleSearchOutputDto> articlesBeforeDelete = readAllArticles();

        this.restService.loginAdmin().restBuilder(new RestBuilder<ArticleDto>()).clazz(ArticleDto.class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).expand("miw").delete().build();

        List<ArticleSearchOutputDto> articlesAfterDelete = readAllArticles();

        assertEquals(articlesBeforeDelete.size(), articlesAfterDelete.size());

    }

    private List<ArticleSearchOutputDto> readAllArticles() {
        return Arrays.asList(this.restService.loginAdmin().restBuilder(new RestBuilder<ArticleSearchOutputDto[]>())
                .clazz(ArticleSearchOutputDto[].class).path(ArticleResource.ARTICLES)
                .get().build());
    }


    @Test
    void testFindArticleByprovider() {
        List<ArticleSearchOutputDto> dtos = Arrays.asList(this.restService.loginAdmin().restBuilder(new RestBuilder<ArticleSearchOutputDto[]>())
                .clazz(ArticleSearchOutputDto[].class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.PROVIDER_ID).path("5c9fe57b8f8e3f5f344caf8f")
                .get().build());
        assertTrue(dtos.size() > 1);
    }

    @Test
    void testCreateFamilySize() {
        ArrayList<String> sizeArray = new ArrayList<String>();
        for(int i = 2; i < 10; i = i+2) {
            sizeArray.add(Integer.toString(i));
        }
        FamilySizeInputDto familySizeInputDto = new FamilySizeInputDto("Ref#1245", "Descripcion", "prv", sizeArray);

        FamilySizeInputDto fsDTO = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<FamilySizeInputDto>()).clazz(FamilySizeInputDto.class)
                .path(ArticleResource.ARTICLES + ArticleResource.FAMILY_SIZES)
                .body(familySizeInputDto)
                .post().build();
        assertEquals("prv", fsDTO.getProvider());
    }
}
