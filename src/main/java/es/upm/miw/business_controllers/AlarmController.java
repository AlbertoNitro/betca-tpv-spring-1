package es.upm.miw.business_controllers;

import es.upm.miw.data_services.DatabaseSeederService;
import es.upm.miw.documents.Alarm;
import es.upm.miw.documents.Article;
import es.upm.miw.dtos.AlarmDto;
import es.upm.miw.exceptions.ConflictException;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.AlarmRepository;
import es.upm.miw.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class AlarmController {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private DatabaseSeederService databaseSeederService;

    @Autowired
    private ArticleRepository articleRepository;

    public List<AlarmDto> readAll(){
        List<AlarmDto> alarmDtoList = new ArrayList<>();
        for(Alarm alarm: this.alarmRepository.findAll()){
            alarmDtoList.add(new AlarmDto(alarm));
        }
        return alarmDtoList;
    }

    public AlarmDto readAlarm(String code) {
        return new AlarmDto( this.alarmRepository.findById(code)
                .orElseThrow( () -> new NotFoundException("Alarm code (" + code + ")") ) );
    }

    public AlarmDto createAlarm(AlarmDto alarmDto) {
        String code = alarmDto.getCode();
        if( code == null || code == "") {
            code = this.databaseSeederService.nextCodeEan();
        }

        if (this.alarmRepository.findById(code).isPresent()){
            throw new ConflictException("Alarm code (" + code + ")");
        }

        Alarm alarm = prepareAlarm(alarmDto, code);
        this.alarmRepository.save(alarm);
        return new AlarmDto(alarm);
    }

    public AlarmDto updateAlarm(AlarmDto alarmDto, String code) {
        Alarm alarm = prepareAlarm(alarmDto, code);
        this.alarmRepository.save(alarm);
        return new AlarmDto(alarm);
    }

    public void delete(String code) {
        Optional<Alarm> a = this.alarmRepository.findById(code);

        if(a.isPresent()) {
            this.alarmRepository.deleteById(code);
        }
    }

    private Alarm prepareAlarm(AlarmDto alarmDto, String code) {
        Alarm alarm = new Alarm();
        alarm.setCode(code);

        Article article = this.articleRepository.findByCode(alarmDto.getRefToArticle());
        if(article!=null) alarm.setArticle(article);
        else throw new NotFoundException("Article (" + alarmDto.getRefToArticle() + ")");

        alarm.setWarning(alarmDto.getWarning());
        alarm.setCritical(alarmDto.getCritical());
        return alarm;
    }
}
