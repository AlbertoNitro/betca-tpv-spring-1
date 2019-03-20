package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.stock_prediction.PeriodicityType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
class ArticleStockPredictionResourceIT {

    @Autowired
    private RestService restService;

    @Test
    void calculateStockPredictionShouldReturnStockPredictionOutputDtoArray() {
        //ArticleResource.StockPredictionOutputDto[] stockPredictionOutputDtos = this.restService.loginAdmin().restBuilder(new RestBuilder<ArticleResource.StockPredictionOutputDto[]>()).clazz(ArticleResource.StockPredictionOutputDto[].class);
        String json = this.restService.loginAdmin().restBuilder(new RestBuilder<String>()).clazz(String.class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).path(ArticleResource.STOCK_PREDICTION).expand(666)
                .param("periodicityType", PeriodicityType.WEEKLY.name())
                .param("periodsNumber", "5")
                .get().build();
        System.out.print(json);
        assertNotNull(json);
    }

    @Test
    void calculateStockPredictionWithoutRequiredRequestParamShouldThrowBadRequest() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> this.restService.loginAdmin().restBuilder(new RestBuilder<String>()).clazz(String.class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).path(ArticleResource.STOCK_PREDICTION).expand(666)
                .get().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void calculateStockPredictionWithPeriodicityTypeInvalidShouldThrowBadRequest() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> this.restService.loginAdmin().restBuilder(new RestBuilder<String>()).clazz(String.class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).path(ArticleResource.STOCK_PREDICTION).expand(666)
                .param("periodicityType", "blabla")
                .param("periodsNumber", "5")
                .get().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void calculateStockPredictionWithPeriodsNumberAsNotNumberShouldThrowBadRequest() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> this.restService.loginAdmin().restBuilder(new RestBuilder<String>()).clazz(String.class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).path(ArticleResource.STOCK_PREDICTION).expand(666)
                .param("periodicityType", PeriodicityType.WEEKLY.name())
                .param("periodsNumber", "blabla")
                .get().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void calculateStockPredictionWithPeriodsNumberAsNegativeNumberShouldThrowBadRequest() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> this.restService.loginAdmin().restBuilder(new RestBuilder<String>()).clazz(String.class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).path(ArticleResource.STOCK_PREDICTION).expand(666)
                .param("periodicityType", PeriodicityType.WEEKLY.name())
                .param("periodsNumber", "-1")
                .get().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void calculateStockPredictionWithPeriodsNumberAs0ShouldThrowBadRequest() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> this.restService.loginAdmin().restBuilder(new RestBuilder<String>()).clazz(String.class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).path(ArticleResource.STOCK_PREDICTION).expand(666)
                .param("periodicityType", PeriodicityType.WEEKLY.name())
                .param("periodsNumber", "0")
                .get().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void calculateStockPredictionWithPeriodicityTypeAsWeeklyAndPeriodsNumberAs27ShouldThrowBadRequest() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> this.restService.loginAdmin().restBuilder(new RestBuilder<String>()).clazz(String.class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).path(ArticleResource.STOCK_PREDICTION).expand(666)
                .param("periodicityType", PeriodicityType.WEEKLY.name())
                .param("periodsNumber", "27")
                .get().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void calculateStockPredictionWithPeriodicityTypeAsMonthlyAndPeriodsNumberAs13ShouldThrowBadRequest() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> this.restService.loginAdmin().restBuilder(new RestBuilder<String>()).clazz(String.class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).path(ArticleResource.STOCK_PREDICTION).expand(666)
                .param("periodicityType", PeriodicityType.MONTHLY.name())
                .param("periodsNumber", "13")
                .get().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void calculateStockPredictionWithPeriodicityTypeAsYearlyAndPeriodsNumberAs3ShouldThrowBadRequest() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> this.restService.loginAdmin().restBuilder(new RestBuilder<String>()).clazz(String.class)
                .path(ArticleResource.ARTICLES).path(ArticleResource.CODE_ID).path(ArticleResource.STOCK_PREDICTION).expand(666)
                .param("periodicityType", PeriodicityType.YEARLY.name())
                .param("periodsNumber", "3")
                .get().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
