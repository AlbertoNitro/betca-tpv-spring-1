package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Role;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.UserDto;
import es.upm.miw.dtos.UserMinimumDto;
import es.upm.miw.dtos.UserProfileDto;
import es.upm.miw.dtos.UserRolesDto;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class UserControllerIT {

    @Autowired
    private UserController userController;
    private User user;
    private User user2;
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void seedDb() {

        this.user = new User("123445", "123445", "666001110","123445","C/ TPV, 100, 1A, 28000 Madrid","user2@gmail.com");
        this.user2 = new User("987654321", "987654", "111");
        this.userRepository.save(user);
        this.userRepository.save(user2);

    }

    @Test
    void testCreateUserMinimum() {
        UserMinimumDto userInputDto = new UserMinimumDto("111222333", "aa");
        UserMinimumDto userMinimumDto = userController.createUserMinimum(new UserMinimumDto(userInputDto.getMobile(), userInputDto.getUsername()));
        assertEquals(userMinimumDto.getMobile(), userInputDto.getMobile());
    }

    @Test
    void testCreateUserMinimumAlreadyExists() {
        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                userController.createUserMinimum(new UserMinimumDto(this.user.getMobile(), this.user.getUsername())));
        assertTrue(thrown.getMessage().contains("Bad Request Exception (400)"));
    }

    @Test
    void testCreateUser() {
        UserDto userInputDto = new UserDto(new User("666547892", "user", "puser",
                "123445","C/ TPV, 100, 1A, 28000 Madrid","user2@gmail.com"));
        UserDto userOutputDto = userController.create(userInputDto);
        assertEquals(userOutputDto.getMobile(), userInputDto.getMobile());
    }

    @Test
    void testCreateUserAlreadyExists() {
        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                userController.create(new UserDto(this.user)));
        assertTrue(thrown.getMessage().contains("Bad Request Exception (400)"));
    }

    @Test
    void testUpdateUser() {
        UserDto userInputDto = new UserDto(this.user2);
        userInputDto.setUsername("differenteUserName");

        UserDto userOutputDto = userController.update(userInputDto.getMobile(), userInputDto);
        assertFalse(userOutputDto.getUsername().equals(this.user2.getUsername()));
    }

    @Test
    void testUpdateUserDifferentMobile() {
        UserDto userInputDto = new UserDto(this.user2);

        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                userController.update("111171111", userInputDto));
        assertTrue(thrown.getMessage().contains("Bad Request Exception (400)"));
    }

    @Test
    void testUpdateUserNotFound() {
        UserDto userInputDto = new UserDto(this.user2);
        userInputDto.setMobile("991982658");

        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                userController.update(userInputDto.getMobile(), userInputDto));
        assertTrue(thrown.getMessage().contains("Not Found Exception (404)"));
    }

    @Test
    void testReadAll() {
        List<UserMinimumDto> users = userController.readAll();
        //System.out.println(users);
        assertTrue(users.size() > 1);
    }

    @Test
    void testUpdateRolesUser() {
        UserRolesDto userRolesDto = new UserRolesDto();
        userRolesDto.setMobile(this.user2.getMobile());
        Role[] rolesUpdate= new Role[]{Role.OPERATOR, Role.MANAGER};
        userRolesDto.setRoles(rolesUpdate);
        UserDto result = this.userController.updateRoles(userRolesDto.getMobile(), userRolesDto);
        assertEquals(rolesUpdate.length, result.getRoles().length);
    }

    @Test
    void testUpdateNotRolesUserFoundMobile() {
        UserRolesDto userRolesDto = new UserRolesDto();
        userRolesDto.setMobile("no-existent-mobile-user");
        assertThrows(NotFoundException.class, () -> this.userController.updateRoles(userRolesDto.getMobile(), userRolesDto));
    }

    @Test
    void testUpdateRolesUserNoMobile() {
        UserRolesDto userRolesDto = new UserRolesDto();
        assertThrows(BadRequestException.class, () -> this.userController.updateRoles(null, userRolesDto));
    }

    @Test
    void testUpdateProfileUser() {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setMobile(this.user.getMobile());
        String updatedpassword = "contraseniaNueva";
        userProfileDto.setPassword(updatedpassword);
        UserProfileDto result = this.userController.updateProfile(userProfileDto.getMobile(), userProfileDto);

    }

    @Test
    void testProfileUserNotFoundMobile() {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setMobile("no-existent-mobile-user");
        assertThrows(NotFoundException.class, () -> this.userController.updateProfile(userProfileDto.getMobile(), userProfileDto));
    }

    @Test
    void testProfileUserNoMobile() {
        UserProfileDto userProfileDto = new UserProfileDto();
        assertThrows(BadRequestException.class, () -> this.userController.updateProfile(null, userProfileDto));
    }

    @Test
    void testreadAllByUsernameDniAddressRoleCustomer() {
        List<UserMinimumDto> users = userController.readAllByUsernameDniAddressRoles(this.user.getMobile(),this.user.getUsername(),this.user.getDni(),this.user.getAddress(),this.user.getRoles());
        assertEquals(this.user.getUsername(), users.get(0).getUsername());
    }
    @Test
    void testreadAllOnlyCustomer() {
        List<UserMinimumDto> users = userController.readAllByUsernameDniAddressRoles("","","","",this.user.getRoles());
        assertEquals(this.user.getUsername(), users.get(0).getUsername());
    }

    @Test
    void testValidatorPassword() {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setMobile(this.user.getMobile());
        userProfileDto.setPassword("666001110");
        Boolean result = this.userController.validatorPassword(userProfileDto.getMobile(), userProfileDto);
        assertEquals(true, result);
    }

    @Test
    void testValidatorIncorrectPassword() {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setMobile(this.user.getMobile());
        userProfileDto.setPassword("");
        Boolean result = this.userController.validatorPassword(userProfileDto.getMobile(), userProfileDto);
        assertEquals(false, result);
    }

    @Test
    void testValidatorPasswordNotFoundMobile() {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setMobile("no-existent-mobile-user");
        assertThrows(NotFoundException.class, () -> this.userController.validatorPassword(userProfileDto.getMobile(), userProfileDto));
    }

    @Test
    void testValidatorPasswordNoMobile() {
        UserProfileDto userProfileDto = new UserProfileDto();
        assertThrows(BadRequestException.class, () -> this.userController.validatorPassword(null, userProfileDto));
    }

    @AfterEach
    void delete() {
        this.userRepository.delete(user);
        this.userRepository.delete(user2);

    }

}
