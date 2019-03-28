package es.upm.miw.rest_controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (AlarmResource.ALARMS)
public class AlarmResource {

    public static final String ALARMS = "/alarms";


    @GetMapping()
    public String readAll() { return "Test"; };

}
