package es.upm.miw.business_controllers;

import es.upm.miw.repositories.TimeClockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TimeClockController {
    @Autowired
    private TimeClockRepository timeClockRepository;

}
