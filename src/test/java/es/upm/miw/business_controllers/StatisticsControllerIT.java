package es.upm.miw.business_controllers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import es.upm.miw.TestConfig;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.output.StatisticDtoOutput;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class StatisticsControllerIT {

    @Autowired
    private StatisticController statisticController;

    private ArticleDto articleDto;

    @BeforeEach
    void seed() {
        this.articleDto = new ArticleDto("non exist", "descrip", "ref", BigDecimal.TEN, null);
    }

    @Test
    void testGetDataTotalSalesPerDayForStatistics() {
        LocalDateTime dateFrom = LocalDateTime.now().minusDays(1);
        LocalDateTime dateTo = LocalDateTime.now().plusDays(1);

        List<StatisticDtoOutput> statisticDtoOutputList = this.statisticController.getDataStatisticTotalSalesPerDay(dateFrom.toLocalDate().toString(), dateTo.toLocalDate().toString());
        assertTrue(statisticDtoOutputList.size() > 0);
    }
}
