package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.TimeClockController;
import es.upm.miw.dtos.input.TimeClockSearchInputDto;
import es.upm.miw.dtos.output.TimeClockOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@PreAuthorize("hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(TimeClockResource.TIMECLOCK)
public class TimeClockResource {
    public static final String TIMECLOCK = "/timeclocks";
    public static final String SEARCH = "/search";

    @Autowired
    private TimeClockController timeClockController;

    @PreAuthorize("authenticated")
    @GetMapping(value = SEARCH)
    public TimeClockOutputDto[] searchByDateRangeAndUserMobile(@RequestParam(required = false) Long dateFromMs, @RequestParam(required = false) Long dateToMs, @Valid @RequestParam(required = false) String mobile) {
        TimeClockSearchInputDto timeClockSearchInputDto = new TimeClockSearchInputDto(dateFromMs, dateToMs, mobile);
        timeClockSearchInputDto.validate();
        return this.timeClockController.searchByDateRangeAndUserMobile(timeClockSearchInputDto);
    }

}
