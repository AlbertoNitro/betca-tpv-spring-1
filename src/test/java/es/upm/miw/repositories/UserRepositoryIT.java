package es.upm.miw.repositories;

import es.upm.miw.documents.Role;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.UserMinimumDto;
import miw.TestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

@TestConfig
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void seedDb() {
        this.user = new User("666001000", "666001000", "666001000");
        this.userRepository.save(user);
    }

    @Test
    public void testFindByMobile() {
        User userBd = userRepository.findByMobile("666001000");
        assertNotNull(userBd);
        assertEquals("666001000", userBd.getUsername());
        assertArrayEquals(new Role[]{Role.CUSTOMER}, userBd.getRoles());
    }

    @Test
    public void testFindCustomerAll() {
        List<UserMinimumDto> userList = userRepository.findCustomerAll();
    }

    @AfterEach
    public void delete() {
        this.userRepository.delete(user);
    }

}
