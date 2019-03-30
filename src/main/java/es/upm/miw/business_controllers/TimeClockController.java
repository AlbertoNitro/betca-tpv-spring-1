package es.upm.miw.business_controllers;

import es.upm.miw.documents.TimeClock;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.input.TimeClockSearchInputDto;
import es.upm.miw.dtos.output.TimeClockOutputDto;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.TimeClockRepository;
import es.upm.miw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Controller
public class TimeClockController {
    @Autowired
    private TimeClockRepository timeClockRepository;

    @Autowired
    private UserRepository userRepository;

    private static final LocalDateTime DEFAULT_LOCAL_DATE_TIME = LocalDateTime.now().minusYears(1);

    public TimeClockOutputDto[] readAll() {
        return convertResultDomainModelToDto(this.timeClockRepository.findAll());
    }

    public TimeClockOutputDto[] searchByDateRangeAndUserMobile(TimeClockSearchInputDto timeClockSearchInputDto) {
        TimeClockOutputDto[] timeClockOutputDtos;
        if (timeClockSearchInputDto.empty()) {
            timeClockOutputDtos = readAll();
        } else {
            LocalDateTime dateFrom = timeClockSearchInputDto.getDateFrom() == null ? DEFAULT_LOCAL_DATE_TIME : timeClockSearchInputDto.getDateFrom();
            LocalDateTime dateTo = timeClockSearchInputDto.getDateTo() == null ? getCurrentLocalDateTime() : timeClockSearchInputDto.getDateTo();
            User user = timeClockSearchInputDto.getUserMobile() == null ? null : getUserTimeClockByMobile(timeClockSearchInputDto.getUserMobile());
            if (user != null) {
                timeClockOutputDtos = convertResultDomainModelToDto(this.timeClockRepository.findByClockinDateBetweenAndUserOrderByClockinDateDesc(dateFrom, dateTo, user.getId()));
            } else {
                timeClockOutputDtos = convertResultDomainModelToDto(this.timeClockRepository.findByClockinDateBetweenOrderByClockinDateDesc(dateFrom, dateTo));
            }
        }
        return timeClockOutputDtos;
    }

    public TimeClock insertTimeClock(TimeClock timeClock) {
        return this.timeClockRepository.save(timeClock);
    }

    public TimeClock updateTimeClock(User user) {
        TimeClock timeClockToUpdate = this.timeClockRepository.findFirst1ByUserOrderByClockinDateDesc(user.getId());
        if (sameDay(timeClockToUpdate.getClockinDate(), timeClockToUpdate.getClockoutDate())) {
            timeClockToUpdate.clockout();
            return this.timeClockRepository.save(timeClockToUpdate);
        } else {
            return timeClockToUpdate;
        }
    }

    private LocalDateTime getCurrentLocalDateTime() {
        return Instant.ofEpochMilli(Instant.now().toEpochMilli()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public User getUserTimeClockByMobile(String mobile) {
        return this.userRepository.findByMobile(mobile)
                .orElseThrow(() -> new NotFoundException("User mobile:" + mobile));
    }

    private boolean sameDay(LocalDateTime dateSource, LocalDateTime dateTarget) {
        return dateSource.toLocalDate().isEqual(dateTarget.toLocalDate());
    }

    private TimeClockOutputDto[] convertResultDomainModelToDto(List<TimeClock> all) {
        TimeClockOutputDto[] timeClockOutputDtos = new TimeClockOutputDto[all.size()];
        int i = 0;
        for (TimeClock timeClock : all) {
            timeClockOutputDtos[i] = new TimeClockOutputDto(timeClock);
            i++;
        }
        return timeClockOutputDtos;
    }

}
