package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.TimeClock;
import es.upm.miw.documents.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class TimeClockRepositoryIT {
    @Autowired
    private TimeClockRepository timeClockRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testReadAll() { assertTrue( this.timeClockRepository.findAll().isEmpty());}

    @Test
    void testFindFirst1ByUserOrderByClockinDateDesc(String id){
        User user = this.userRepository.findByMobile("666666000").orElse(null);
        TimeClock timeClock = this.timeClockRepository.findFirst1ByUserOrderByClockinDateDesc(user.getId());
        assertTrue(timeClock == null);
    }

    @Test
    void testFindByClockinDateBetweenAndUserOrderByClockinDateDesc(){
        LocalDateTime dateFrom = LocalDateTime.now().minusDays(2);
        LocalDateTime dateTo = LocalDateTime.now().plusDays(2);
        User user = this.userRepository.findByMobile("666666000").orElse(null);
        List<TimeClock> timeClocks;
        timeClocks = this.timeClockRepository.findByClockinDateBetweenAndUserOrderByClockinDateDesc(dateFrom, dateTo, user.getId());
        assertTrue(timeClocks.isEmpty());
    }
}
