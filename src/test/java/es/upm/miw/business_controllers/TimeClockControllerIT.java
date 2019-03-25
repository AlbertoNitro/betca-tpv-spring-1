package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.TimeClock;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.input.TimeClockSearchInputDto;
import es.upm.miw.dtos.output.TimeClockOutputDto;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class TimeClockControllerIT {

    @Autowired
    private TimeClockController timeClockController;

    private TimeClock timeClock1;

    private TimeClock timeClock2;

    private TimeClock timeClock3;

    private User user;

    @BeforeEach
    void seedDb() {
        this.user = this.timeClockController.getUserTimeClockByMobile("666666000");
        this.timeClock1 = new TimeClock(user);
        this.timeClock2 = new TimeClock(user);
        this.timeClock3 = new TimeClock(user);
    }

    @Test
    void testReadAll() {
        TimeClockOutputDto[] timeClocksOutput = this.timeClockController.readAll();
        assertTrue(timeClocksOutput.length >= 0);
    }

    @Test
    void testSearchByDateRangeAndUserMobile() {
        Long dateFromMs = Instant.now().toEpochMilli();
        Long dateToMs = Instant.now().plus(8, ChronoUnit.HOURS).toEpochMilli();
        String mobile = "666666000";
        TimeClockSearchInputDto timeClockSearchInputDto = new TimeClockSearchInputDto(dateFromMs, dateToMs, mobile);
        TimeClockOutputDto[] timeClocksOutput = this.timeClockController.searchByDateRangeAndUserMobile(timeClockSearchInputDto);
        assertTrue(timeClocksOutput.length >= 0);
    }

    @Test
    void testCreateTimeClock() {
        TimeClock timeClockCreated = this.timeClockController.insertTimeClock(timeClock1);
        assertEquals(timeClockCreated.getUser(), timeClock1.getUser());
        assertNotNull(timeClockCreated.getClockinDate());
        assertNotNull(timeClockCreated.getTotalHours());
        LogManager.getLogger().debug(">>>>> TimeClock clockin: " + timeClockCreated.getClockinDate() + " total hours: " + timeClockCreated.getTotalHours());

        timeClockCreated = this.timeClockController.insertTimeClock(timeClock2);
        assertEquals(timeClockCreated.getUser(), timeClock2.getUser());
        assertNotNull(timeClockCreated.getClockinDate());
        assertNotNull(timeClockCreated.getTotalHours());
        LogManager.getLogger().debug(">>>>> TimeClock clockin: " + timeClockCreated.getClockinDate() + " total hours: " + timeClockCreated.getTotalHours());
    }
}
