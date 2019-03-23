package es.upm.miw.rest_controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(TimeClockResource.TIMECLOCK)
public class TimeClockResource {
    public static final String TIMECLOCK = "/timeclocks";

}
