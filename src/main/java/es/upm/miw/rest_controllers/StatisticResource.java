package es.upm.miw.rest_controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.upm.miw.business_controllers.StatisticController;
import es.upm.miw.dtos.output.StatisticDtoOutput;
import es.upm.miw.exceptions.BadRequestException;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
@RestController
@RequestMapping(StatisticResource.STATISTIC)
public class StatisticResource {

    public static final String STATISTIC = "/statistics";

    public static final String STATISTIC_NAME = "/{statisticName}";

    @Autowired
    private StatisticController statisticController;

    @PreAuthorize("authenticated")
    @GetMapping(value = STATISTIC_NAME)
    public List<StatisticDtoOutput> read(@PathVariable String statisticName, @RequestParam String dateFrom, @RequestParam String dateTo) {
        List<StatisticDtoOutput> dataReturn = new ArrayList<>();
        if (statisticName.equalsIgnoreCase("total-sales-per-day")) {
            dataReturn = this.statisticController.getDataStatisticTotalSalesPerDay(dateFrom, dateTo);
        } else if (statisticName.equalsIgnoreCase("average-daily-expense")) {
            dataReturn = this.statisticController.getDataStatisticAverageDailyExpense(dateFrom, dateTo);
        } else {
            throw new BadRequestException("Not allowed value: " + statisticName);
        }
        return dataReturn;
    }
}
