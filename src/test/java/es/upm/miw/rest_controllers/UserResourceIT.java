package es.upm.miw.rest_controllers;

import es.upm.miw.documents.Role;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.UserDto;
import es.upm.miw.dtos.UserMinimumDto;
import es.upm.miw.dtos.UserQueryDto;
import es.upm.miw.dtos.UserRolesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
class UserResourceIT {

    @Autowired
    private RestService restService;

    private UserRolesDto existentUser;

    @BeforeEach
    void before() {
        List<UserRolesDto> users = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<UserRolesDto[]>()).clazz(UserRolesDto[].class)
                .path(UserResource.USERS)
                .get().build());
        this.existentUser = users.get(7);
    }
    @Test
    void testLogin() {
        this.restService.loginAdmin();
        assertTrue(this.restService.getTokenDto().getToken().length() > 10);
    }

    @Test
    void testLoginAdminPassNull() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.logout().restBuilder().basicAuth(restService.getAdminMobile(), "kk")
                        .path(UserResource.USERS).path(UserResource.TOKEN).post().build()
        );
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void testLoginNonMobile() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.logout().restBuilder().basicAuth("1", "kk")
                        .path(UserResource.USERS).path(UserResource.TOKEN).post().build()
        );
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void testReadAdminAdmin() {
        UserDto userDto = this.restService.loginAdmin().restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
                .path(UserResource.USERS).path(UserResource.MOBILE_ID).expand(this.restService.getAdminMobile())
                .get().build();
        assertNotNull(userDto);
    }

    @Test
    void testReadManagerOperator() {
        UserDto userDto = this.restService.loginManager().restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
                .path(UserResource.USERS).path(UserResource.MOBILE_ID).expand("666666002")
                .get().build();
        assertNotNull(userDto);
    }

    @Test
    void testReadOperatorCustomer() {
        UserDto userDto = this.restService.loginOperator().restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
                .path(UserResource.USERS).path(UserResource.MOBILE_ID).expand("666666004")
                .get().build();
        assertNotNull(userDto);
    }

    @Test
    void testReadOperatorOperator() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginOperator().restBuilder(new RestBuilder<User>()).clazz(User.class).log()
                        .path(UserResource.USERS).path(UserResource.MOBILE_ID).expand("666666003")
                        .get().build());
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void testReadAll() {
        List<UserMinimumDto> userMinimumDtoList = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<UserMinimumDto[]>()).clazz(UserMinimumDto[].class)
                .path(UserResource.USERS)
                .get().build());
        assertTrue(userMinimumDtoList.size() > 1);
    }

    private RestBuilder<UserRolesDto> restUpdateRolesBuilder(String mobile, UserRolesDto userRolesDto) {
        return this.restService.loginAdmin()
                .restBuilder(new RestBuilder<UserRolesDto>()).clazz(UserRolesDto.class)
                .path(UserResource.USERS).path(UserResource.ROLES).path("/" + mobile)
                .body(userRolesDto)
                .put();
    }

    @Test
    void testUpdateRoles() {

        UserRolesDto userRolesDto = new UserRolesDto();
        userRolesDto.setMobile(this.existentUser.getMobile());
        Role[] rolesUpdate= new Role[]{Role.MANAGER};
        userRolesDto.setRoles(rolesUpdate);
        UserRolesDto result = restUpdateRolesBuilder(existentUser.getMobile(), userRolesDto).build();
        assertEquals(rolesUpdate.length, result.getRoles().length);
    }

    @Test
    void testUpdateNullMobile() {
        UserRolesDto userRolesDto = new UserRolesDto();
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                restUpdateRolesBuilder(userRolesDto.getId(), userRolesDto).build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testreadQueryByAdmin() {

        UserQueryDto userQueryInputDto = new UserQueryDto();
        List<UserMinimumDto> userMinimumDtoList = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<UserMinimumDto[]>()).clazz(UserMinimumDto[].class)
                .path(UserResource.USERS).path(UserResource.QUERY)
                .body(userQueryInputDto).post().build());
        System.out.println(userMinimumDtoList);
        assertTrue(userMinimumDtoList.size() > 1);
    }



    @Test
    void testreadQueryByManager() {
        UserQueryDto userQueryInputDto = new UserQueryDto();
        List<UserMinimumDto> userMinimumDtoList = Arrays.asList(this.restService.loginManager()
                .restBuilder(new RestBuilder<UserMinimumDto[]>()).clazz(UserMinimumDto[].class)
                .path(UserResource.USERS).path(UserResource.QUERY)
                .body(userQueryInputDto).post().build());
        assertTrue(userMinimumDtoList.size() > 1);
    }

    @Test
    void testreadQueryByOperator() {

        UserQueryDto userQueryInputDto = new UserQueryDto();
        List<UserMinimumDto> userMinimumDtoList = Arrays.asList(this.restService.loginOperator()
                .restBuilder(new RestBuilder<UserMinimumDto[]>()).clazz(UserMinimumDto[].class)
                .path(UserResource.USERS).path(UserResource.QUERY)
                .body(userQueryInputDto).post().build());
        assertTrue(userMinimumDtoList.size() > 1);
    }

    @Test
    void testreadQueryByOnlyCustomer() {

        UserQueryDto userQueryInputDto = new UserQueryDto();
        userQueryInputDto.setOnlyCustomer(true);
        List<UserMinimumDto> userMinimumDtoList = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<UserMinimumDto[]>()).clazz(UserMinimumDto[].class)
                .path(UserResource.USERS).path(UserResource.QUERY)
                .body(userQueryInputDto).post().build());
        System.out.println(userMinimumDtoList.size());
        assertTrue(userMinimumDtoList.size() > 1);
    }
    @Test
    void testreadMobileByAdmin() {

        UserDto userDto = this.restService.loginAdmin().restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
                .path(UserResource.USERS).path(UserResource.MOBILE_ID).expand("1987654321")
                .get().build();
        UserQueryDto userQueryInputDto = new UserQueryDto();
        userQueryInputDto.setMobile(userDto.getMobile());

        List<UserMinimumDto> userMinimumDtoList = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<UserMinimumDto[]>()).clazz(UserMinimumDto[].class)
                .path(UserResource.USERS).path(UserResource.QUERY)
                .body(userQueryInputDto).post().build());
        assertEquals(1,userMinimumDtoList.size());
    }
    @Test
    void testreadMobileUserNameByAdmin() {

        UserDto userDto = this.restService.loginAdmin().restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
                .path(UserResource.USERS).path(UserResource.MOBILE_ID).expand("1987654321")
                .get().build();
        UserQueryDto userQueryInputDto = new UserQueryDto();
        userQueryInputDto.setMobile(userDto.getMobile());
        userQueryInputDto.setUsername(userDto.getUsername());

        List<UserMinimumDto> userMinimumDtoList = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<UserMinimumDto[]>()).clazz(UserMinimumDto[].class)
                .path(UserResource.USERS).path(UserResource.QUERY)
                .body(userQueryInputDto).post().build());
        assertEquals(1,userMinimumDtoList.size());
    }
    @Test
    void testreadMobileUserNameDniByAdmin() {

        UserDto userDto = this.restService.loginAdmin().restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
                .path(UserResource.USERS).path(UserResource.MOBILE_ID).expand("1987654321")
                .get().build();
        UserQueryDto userQueryInputDto = new UserQueryDto();
        userQueryInputDto.setMobile(userDto.getMobile());
        userQueryInputDto.setUsername(userDto.getUsername());
        userQueryInputDto.setDni(userDto.getDni());

        List<UserMinimumDto> userMinimumDtoList = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<UserMinimumDto[]>()).clazz(UserMinimumDto[].class)
                .path(UserResource.USERS).path(UserResource.QUERY)
                .body(userQueryInputDto).post().build());
        assertEquals(1,userMinimumDtoList.size());
    }

    @Test
    void testreadAddressByAdmin() {

        UserQueryDto userQueryInputDto = new UserQueryDto();
        userQueryInputDto.setAddress("Madrid");
        List<UserMinimumDto> userMinimumDtoList = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<UserMinimumDto[]>()).clazz(UserMinimumDto[].class)
                .path(UserResource.USERS).path(UserResource.QUERY)
                .body(userQueryInputDto).post().build());

        assertTrue(userMinimumDtoList.size()>1);
    }
    @Test
    void testreadAddressOnlyCustomer() {

        UserQueryDto userQueryInputDto = new UserQueryDto();
        userQueryInputDto.setAddress("Madrid");
        userQueryInputDto.setOnlyCustomer(true);
        List<UserMinimumDto> userMinimumDtoList = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<UserMinimumDto[]>()).clazz(UserMinimumDto[].class)
                .path(UserResource.USERS).path(UserResource.QUERY)
                .body(userQueryInputDto).post().build());
        System.out.println(userMinimumDtoList);
        assertEquals(2,userMinimumDtoList.size());
    }

    @Test
    void testreadUserNameAddressOnlyCustomer() {

        UserQueryDto userQueryInputDto = new UserQueryDto();
        userQueryInputDto.setUsername("u");
        userQueryInputDto.setAddress("Madrid");
        userQueryInputDto.setOnlyCustomer(true);
        List<UserMinimumDto> userMinimumDtoList = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<UserMinimumDto[]>()).clazz(UserMinimumDto[].class)
                .path(UserResource.USERS).path(UserResource.QUERY)
                .body(userQueryInputDto).post().build());
        System.out.println(userMinimumDtoList);
        assertEquals(2,userMinimumDtoList.size());
    }
}
