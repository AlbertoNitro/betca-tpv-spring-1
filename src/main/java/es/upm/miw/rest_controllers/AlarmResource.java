package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.AlarmController;
import es.upm.miw.dtos.AlarmDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasRole ('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping (AlarmResource.ALARMS)
public class AlarmResource {

    public static final String ALARMS = "/alarms";
    public static final  String ALARM_ID = "/{id}";

    @Autowired
    private AlarmController alarmController;

    @GetMapping()
    public List<AlarmDto> readAll() { return this.alarmController.readAll(); };

    @GetMapping(value = AlarmResource.ALARM_ID)
    public AlarmDto readAlarm(@PathVariable String id){
        return alarmController.readAlarm(id);
    }

    @PostMapping()
    public AlarmDto createAlarm(@Valid @RequestBody AlarmDto alarmDto) {
        return alarmController.createAlarm(alarmDto);
    }

    @PutMapping(value = ALARM_ID)
    public AlarmDto updateAlarm(@PathVariable String id, @Valid @RequestBody AlarmDto alarmDto){
        return alarmController.updateAlarm(alarmDto, id);
    }

    @DeleteMapping( value = AlarmResource.ALARM_ID)
    public void delete(@PathVariable String id) {
        alarmController.delete(id);
    }

}
