package es.upm.miw.dataServices;

import es.upm.miw.documents.User;
import es.upm.miw.repositories.UserRepository;
import miw.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class DatabaseSeederServiceIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseSeederService databaseSeederService;

    @Test
    public void testUserSeedDatabase() {
        User user = userRepository.findByMobile("666666001");
        assertNotNull(user);
        assertEquals("u001", user.getUsername());
        assertEquals("u001@gmail.com", user.getEmail());
        assertEquals("66666600L", user.getDni());
        assertTrue(user.getRoles().length >= 2);
    }

    @Test
    public void testCreateEan13() {
        String code = this.databaseSeederService.createEan13();
        assertEquals("84", code.substring(0, 2));
        assertNotEquals(code, this.databaseSeederService.createEan13());
    }

}
