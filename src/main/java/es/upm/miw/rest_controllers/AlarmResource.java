package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.AlarmController;
import es.upm.miw.dtos.AlarmDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@PreAuthorize("hasRole ('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping (AlarmResource.ALARMS)
public class AlarmResource {

    public static final String ALARMS = "/alarms";
    public static final  String ALARM_ID = "/{id}";

    @Autowired
    private AlarmController alarmController;

    @GetMapping()
    public String readAll() { return "Test"; };

    @GetMapping(value = AlarmResource.ALARM_ID)
    public AlarmDto readAlarm(@PathVariable String id){
        return alarmController.readAlarm(id);
    }


}
