package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Role;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.UserMinimumDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@TestConfig
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User user2;
    private User user3;

    @BeforeEach
    void seedDb() {
        this.user = new User("666001000", "666001000", "666001000");
        this.userRepository.save(user);

        this.user2 = new User("123445", "123445", "666001110","123445","C/ TPV, 100, 1A, 28000 Madrid","user2@gmail.com");
        this.userRepository.save(user2);
        this.user3 = new User("1234457", "1234457", "666001111","1234457","C/ TPV, 100, 1A, 28000 Madrid","user3@gmail.com");
        this.userRepository.save(user3);
    }

    @Test
    void testFindByMobile() {
        User userBd = userRepository.findByMobile("666001000").get();
        assertEquals("666001000", userBd.getUsername());
        assertArrayEquals(new Role[]{Role.CUSTOMER}, userBd.getRoles());
    }

    @Test
    void testFindCustomerAll() {
        List<UserMinimumDto> userList = userRepository.findAllUsers();
    }

    @Test
    void testfindByOnlyCustomer() {
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniAddressLikeNullSafeandRoles("","","","",this.user.getRoles());
        assertEquals(4, userList.size());
    }
    @Test
    void testfindByAdressAndRole() {
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniAddressLikeNullSafeandRoles("","","",this.user2.getAddress(),this.user2.getRoles());
        assertEquals(2, userList.size());
    }

    @Test
    void testfindByMobileLikeNullSafeAndRoles() {
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniAddressLikeNullSafeandRoles(this.user2.getMobile(),"","","",this.user2.getRoles());
        assertEquals(2, userList.size());
    }
    @Test
    void testfindByMobileUsernameLikeNullSafeAndRoles() {
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniAddressLikeNullSafeandRoles(this.user2.getMobile(),this.user2.getUsername(),"","",this.user2.getRoles());
        assertEquals(2, userList.size());
    }
    @Test
    void testfindByMobileUsernameDniLikeNullSafeAndRoles() {
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniAddressLikeNullSafeandRoles(this.user2.getMobile(),this.user2.getUsername(),this.user2.getDni(),"",this.user2.getRoles());
        assertEquals(2, userList.size());
    }
    @Test
    void testfindByMobileUsernameDniAddressLikeNullSafeAndRoles() {
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniAddressLikeNullSafeandRoles(this.user3.getMobile(),this.user3.getUsername(),this.user3.getDni(),this.user3.getAddress(),this.user3.getRoles());
        assertEquals(1, userList.size());
    }

    @AfterEach
    void delete() {
        this.userRepository.delete(user);
        this.userRepository.delete(user2);
        this.userRepository.delete(user3);
    }

}
