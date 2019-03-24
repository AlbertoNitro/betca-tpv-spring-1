package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.dtos.input.TimeClockSearchInputDto;
import es.upm.miw.dtos.output.TimeClockOutputDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class TimeClockControllerIT {

    @Autowired
    private TimeClockController timeClockController;

    @Test
    void testReadAll() {
        TimeClockOutputDto[] timeClocksOutput = this.timeClockController.readAll();
        assertTrue(timeClocksOutput.length == 0);
    }

    @Test
    void testSearchByDateRangeAndUserMobile() {
        Long dateFromMs = Instant.now().toEpochMilli();
        Long dateToMs = Instant.now().plus(8, ChronoUnit.HOURS).toEpochMilli();
        String mobile = "666666000";
        TimeClockSearchInputDto timeClockSearchInputDto = new TimeClockSearchInputDto(dateFromMs, dateToMs, mobile);
        TimeClockOutputDto[] timeClocksOutput = this.timeClockController.searchByDateRangeAndUserMobile(timeClockSearchInputDto);
        assertTrue(timeClocksOutput.length == 0);
    }
}
