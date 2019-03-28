package es.upm.miw.business_controllers;

import es.upm.miw.dtos.AlarmDto;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AlarmController {

    @Autowired
    private AlarmRepository alarmRepository;


    public AlarmDto readAlarm(String code) {
        return new AlarmDto( this.alarmRepository.findById(code)
                .orElseThrow( () -> new NotFoundException("Alarm code (" + code + ")") ) );
    }
}
