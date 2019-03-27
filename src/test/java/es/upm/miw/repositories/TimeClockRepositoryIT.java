package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.TimeClock;
import es.upm.miw.documents.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class TimeClockRepositoryIT {
    @Autowired
    private TimeClockRepository timeClockRepository;

    @Autowired
    private UserRepository userRepository;

    private TimeClock timeClock1;

    private TimeClock timeClock2;

    private TimeClock timeClock3;

    private User user;

    @BeforeEach
    void seedDb() {
        this.user = this.userRepository.findByMobile("666666001").orElse(null);
        this.timeClock1 = new TimeClock(user);
        this.timeClock2 = new TimeClock(user);
        this.timeClock3 = new TimeClock(user);
    }

    @Test
    void testCreateTimeClock() {
        TimeClock timeClockCreated = this.timeClockRepository.save(timeClock1);
        assertEquals(timeClockCreated.getUser(), timeClock1.getUser());
        assertNotNull(timeClockCreated.getClockinDate());
        assertNotNull(timeClockCreated.getTotalHours());

        timeClockCreated = this.timeClockRepository.save(timeClock2);
        assertEquals(timeClockCreated.getUser(), timeClock2.getUser());
        assertNotNull(timeClockCreated.getClockinDate());
        assertNotNull(timeClockCreated.getTotalHours());
    }

    @Test
    void testReadAll() {
        assertFalse(this.timeClockRepository.findAll().isEmpty());
    }

    @Test
    void testFindByClockinDateBetweenAndUserOrderByClockinDateDesc() {
        LocalDateTime dateFrom = LocalDateTime.now().minusDays(2);
        LocalDateTime dateTo = LocalDateTime.now().plusDays(2);
        User user = this.userRepository.findByMobile("666666001").orElse(null);
        List<TimeClock> timeClocks;
        timeClocks = this.timeClockRepository.findByClockinDateBetweenAndUserOrderByClockinDateDesc(dateFrom, dateTo, user.getId());
        assertFalse(timeClocks.isEmpty());
    }

    @Test
    void testFindByClockinDateBetweenOrderByClockinDateDesc() {
        LocalDateTime dateFrom = LocalDateTime.now().minusDays(2);
        LocalDateTime dateTo = LocalDateTime.now().plusDays(2);
        List<TimeClock> timeClocks;
        timeClocks = this.timeClockRepository.findByClockinDateBetweenOrderByClockinDateDesc(dateFrom, dateTo);
        assertFalse(timeClocks.isEmpty());
    }
}
