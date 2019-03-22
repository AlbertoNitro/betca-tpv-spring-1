package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class TimeClockRepositoryIT {
    @Autowired
    private TimeClockRepository timeClockRepository;

    @Test
    void testReadAll() { assertTrue( this.timeClockRepository.findAll().isEmpty());}
}
