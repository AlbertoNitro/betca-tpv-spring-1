package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.input.ArticleSearchInputDto;
import es.upm.miw.dtos.input.TimeClockSearchInputDto;
import es.upm.miw.dtos.output.ArticleSearchOutputDto;
import es.upm.miw.dtos.output.TimeClockOutputDto;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
public class TimeClockResourceIT {

    @Autowired
    private RestService restService;

    @Test
    void testSearchByDateRangeAndUserMobile(){
        String dateFromMs = "" + Instant.now().toEpochMilli();
        String dateToMs = "" + Instant.now().plus(8, ChronoUnit.HOURS).toEpochMilli();
        String mobile = "666666000";
        List<TimeClockOutputDto> timeClocks = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TimeClockOutputDto[]>()).clazz(TimeClockOutputDto[].class)
                .path(TimeClockResource.TIMECLOCK).path(TimeClockResource.SEARCH)
                .param("dateFromMs", dateFromMs)
                .param("dateToMs", dateToMs)
                .param("mobile", mobile)
                .get().build());
        assertNotNull(timeClocks);
    }

    @Test
    void testSearchByDateRangeAndUserMobileWithoutRequestParamValues(){
        List<TimeClockOutputDto> timeClocks = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TimeClockOutputDto[]>()).clazz(TimeClockOutputDto[].class)
                .path(TimeClockResource.TIMECLOCK).path(TimeClockResource.SEARCH)
                .param("dateFromMs", null)
                .param("dateToMs", null)
                .param("mobile", null)
                .get().build());
        assertNotNull(timeClocks);
    }

    @Test
    void testSearchByDateRangeAndUserMobileWithMobileRequestParamAndWithoutDateRangeRequestParam(){
        List<TimeClockOutputDto> timeClocks = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TimeClockOutputDto[]>()).clazz(TimeClockOutputDto[].class)
                .path(TimeClockResource.TIMECLOCK).path(TimeClockResource.SEARCH)
                .param("dateFromMs", null)
                .param("dateToMs", null)
                .param("mobile", "666666000")
                .get().build());
        assertNotNull(timeClocks);
    }

    @Test
    void testSearchByDateRangeAndUserMobileWithWrongRequestParamShouldThrowBadRequest(){
        String dateFromMs = "" + Instant.now().plus(8, ChronoUnit.HOURS).toEpochMilli();
        String dateToMs = "" + Instant.now().toEpochMilli();
        String mobile = "666666000";
        //LogManager.getLogger().debug(">>>>> DateFrom : " + dateFromMs + " DateTo: " + dateToMs + " userMobile: " + mobile);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TimeClockOutputDto[]>()).clazz(TimeClockOutputDto[].class)
                .path(TimeClockResource.TIMECLOCK).path(TimeClockResource.SEARCH)
                .param("dateFromMs", dateFromMs)
                .param("dateToMs", dateToMs)
                .param("mobile", mobile)
                .get().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testSearchByDateRangeAndUserMobileWithWrongMobileRequestParamShouldThrowBadRequest(){
        String dateFromMs = "" + Instant.now().toEpochMilli();
        String dateToMs = "" + Instant.now().plus(8, ChronoUnit.HOURS).toEpochMilli();
        String mobile = "666000";
        //LogManager.getLogger().debug(">>>>> DateFrom : " + dateFromMs + " DateTo: " + dateToMs + " userMobile: " + mobile);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TimeClockOutputDto[]>()).clazz(TimeClockOutputDto[].class)
                .path(TimeClockResource.TIMECLOCK).path(TimeClockResource.SEARCH)
                .param("dateFromMs", dateFromMs)
                .param("dateToMs", dateToMs)
                .param("mobile", mobile)
                .get().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
