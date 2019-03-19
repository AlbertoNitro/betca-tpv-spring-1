package es.upm.miw.business_controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import es.upm.miw.TestConfig;
import es.upm.miw.dtos.output.StatisticDtoOutput;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class StatisticsControllerIT {

    @Autowired
    private StatisticController statisticController;

    @Test
    void testGetDataTotalSalesPerDayForStatistics() {
        LocalDateTime dateFrom = LocalDateTime.now().minusDays(1).withNano(0);
        LocalDateTime dateTo = LocalDateTime.now().plusDays(1).withNano(0);

        List<StatisticDtoOutput> statisticDtoOutputList = this.statisticController
                .getDataStatisticTotalSalesPerDay(dateFrom.toString(), dateTo.toString());
        assertTrue(statisticDtoOutputList.size() > 0);
    }

    @Test
    void testGetAverageDailyExpenseForStatistics() {
        LocalDateTime dateFrom = LocalDateTime.now().minusDays(1).withNano(0);
        LocalDateTime dateTo = LocalDateTime.now().plusDays(1).withNano(0);

        List<StatisticDtoOutput> statisticDtoOutputList = this.statisticController
                .getDataStatisticAverageDailyExpense(dateFrom.toString(), dateTo.toString());
        assertTrue(statisticDtoOutputList.size() > 0);
    }
}
