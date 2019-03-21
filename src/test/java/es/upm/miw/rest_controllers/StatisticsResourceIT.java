package es.upm.miw.rest_controllers;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import es.upm.miw.dtos.output.StatisticDtoOutput;
import es.upm.miw.exceptions.BadRequestException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ApiTestConfig
class StatisticsResourceIT {

    @Autowired
    private RestService restService;

    @Test
    void testGetTotalSalesPerDay() {
        LocalDateTime dateFrom = LocalDateTime.of(2019, 3, 1, 0, 0, 0);
        LocalDateTime dateTo = LocalDateTime.of(2019, 3, 30, 23, 59, 59);

        StatisticDtoOutput[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<StatisticDtoOutput[]>().clazz(StatisticDtoOutput[].class))
                .path(StatisticResource.STATISTIC)
                .path(StatisticResource.STATISTIC_NAME)
                .expand("total-sales-per-day")
                .param("dateFrom", dateFrom.toString())
                .param("dateTo", dateTo.toString()).get().build();
        assertEquals(1, results.length);
        assertEquals(100.88, results[0].getValue());
    }

    @Test
    void testGetAverageDailyExpense() {
        LocalDateTime dateFrom = LocalDateTime.of(2019, 3, 1, 0, 0, 0);
        LocalDateTime dateTo = LocalDateTime.of(2019, 3, 30, 23, 59, 59);

        StatisticDtoOutput[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<StatisticDtoOutput[]>().clazz(StatisticDtoOutput[].class))
                .path(StatisticResource.STATISTIC)
                .path(StatisticResource.STATISTIC_NAME)
                .expand("average-daily-expense")
                .param("dateFrom", dateFrom.toString())
                .param("dateTo", dateTo.toString()).get().build();
        assertEquals(1, results.length);
        assertEquals(33.626666666666665, results[0].getValue());
    }

    @Test
    void testErrorValue() {
        LocalDateTime dateFrom = LocalDateTime.of(2019, 3, 1, 0, 0, 0);
        LocalDateTime dateTo = LocalDateTime.of(2019, 3, 30, 23, 59, 59);

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin()
                        .restBuilder(new RestBuilder<StatisticDtoOutput[]>().clazz(StatisticDtoOutput[].class))
                        .path(StatisticResource.STATISTIC)
                        .path(StatisticResource.STATISTIC_NAME)
                        .expand("not-correct-value")
                        .param("dateFrom", dateFrom.toString())
                        .param("dateTo", dateTo.toString()).get().build());

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
