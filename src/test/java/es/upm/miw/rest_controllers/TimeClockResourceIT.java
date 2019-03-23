package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.input.TimeClockSearchInputDto;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ApiTestConfig
public class TimeClockResourceIT {

    @Autowired
    private RestService restService;

    @Test
    void testReadAll() {
        TimeClockSearchInputDto timeClockSearchInputDto = new TimeClockSearchInputDto(LocalDateTime.now(), LocalDateTime.now().plusHours(8), "666666000");
        LogManager.getLogger().debug(">>>>> TimeClockSearchInputDto DateFrom : " + timeClockSearchInputDto.getDateFrom().toString() + " DateTo: " + timeClockSearchInputDto.getDateTo().toString() + "userMobile: " + timeClockSearchInputDto.getUserMobile());
        assertEquals("666666000", timeClockSearchInputDto.getUserMobile());
    }
}
