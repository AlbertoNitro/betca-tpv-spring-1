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
import static org.junit.Assert.assertTrue;

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

        this.user2 = new User("123445", "123445", "666001110","123445","10","C/ TPV, 100, 1A, 28000 Madrid","user2@gmail.com");
        this.userRepository.save(user2);
        this.user3 = new User("1234457", "1234457", "666001111","1234457","10","C/ TPV, 100, 1A, 28000 Madrid","user3@gmail.com");
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
        assertTrue(userList.size()>1);
    }

    @Test
    void testfindByOnlyCustomer() {
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniDiscountAddressLikeNullSafeandRoles("","","","","",this.user.getRoles());
        assertEquals(2, userList.size());
    }
    @Test
    void testfindByAdressAndOnlyCustomer() {
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniDiscountAddressLikeNullSafeandRoles("","","",this.user2.getDiscount(),this.user2.getAddress(),this.user2.getRoles());
        assertEquals(2, userList.size());
    }

    @Test
    void testfindByMobileLikeNullSafeAndOnlyCustomer() {
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniDiscountAddressLikeNullSafeandRoles(this.user2.getMobile(),"","","","",this.user2.getRoles());
        assertEquals(2, userList.size());
    }
    @Test
    void testfindByMobileUsernameLikeNullSafeAndOnlyCustomer() {
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniDiscountAddressLikeNullSafeandRoles(this.user2.getMobile(),this.user2.getUsername(),"","","",this.user2.getRoles());
        assertEquals(2, userList.size());
    }
    @Test
    void testfindByMobileUsernameDniLikeNullSafeAndRoles() {
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniDiscountAddressLikeNullSafeandRoles(this.user2.getMobile(),this.user2.getUsername(),this.user2.getDni(),this.user2.getDiscount(),this.user2.getAddress(),this.user2.getRoles());
        assertEquals(2, userList.size());
    }
    @Test
    void testfindByMobileUsernameDniAddressLikeNullSafeAndOnlyCustomer() {
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniDiscountAddressLikeNullSafeandRoles(this.user3.getMobile(),this.user3.getUsername(),this.user3.getDni(),this.user3.getDiscount(),this.user3.getAddress(),this.user3.getRoles());
        assertEquals(1, userList.size());
    }

    @Test
    void testfindByManagerOperator() {
        User userBd = userRepository.findByMobile("099738470").get();
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniDiscountAddressLikeNullSafeandRoles("","","","","",userBd.getRoles());
        assertEquals(9, userList.size());
    }

    @Test
    void testfindByAdminManagerOperator() {
        User userBd = userRepository.findByMobile("1987654321").get();
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniDiscountAddressLikeNullSafeandRoles("","","","","",userBd.getRoles());
        assertTrue(userList.size()>1);
    }
    @Test
    void testfindByAddressLikeNullSafeManagerOperator() {
        User userBd = userRepository.findByMobile("099738470").get();
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniDiscountAddressLikeNullSafeandRoles("","","","","Cuenca",userBd.getRoles());
        assertEquals(6, userList.size());
    }

    @Test
    void testfindByUsernameilLikeNullSafeOperator() {
        User userBd = userRepository.findByMobile("666666120").get();
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniDiscountAddressLikeNullSafeandRoles("","prueba","","","",userBd.getRoles());
        assertEquals(5, userList.size());
    }

    @Test
    void testfindByUsernameilLikeNullSafeManager() {
        User userBd = userRepository.findByMobile("666666122").get();
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniDiscountAddressLikeNullSafeandRoles("","prueba","","","",userBd.getRoles());
        assertEquals(3, userList.size());
    }

    @Test
    void testfindByMobileUsernameilLikeNullSafeOperator() {
        User userBd = userRepository.findByMobile("666666003").get();
        List<UserMinimumDto> userList = userRepository.findByMobileUsernameDniDiscountAddressLikeNullSafeandRoles("666","u00","", "","",userBd.getRoles());
        assertEquals(2, userList.size());
    }
    @AfterEach
    void delete() {
        this.userRepository.delete(user);
        this.userRepository.delete(user2);
        this.userRepository.delete(user3);
    }

}
