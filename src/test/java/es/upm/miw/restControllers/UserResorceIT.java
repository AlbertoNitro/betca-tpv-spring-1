package es.upm.miw.restControllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
class UserResorceIT {

    @Autowired
    private RestService restService;

    @Test
    void testLogin() {
        this.restService.loginAdmin();
        assertTrue(this.restService.getTokenDto().getToken().length() > 10);
    }

    @Test
    void testLoginAdminPassNull() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                restService.logout().restBuilder().basicAuth(restService.getAdminMobile(), "kk")
                        .path(UserResource.USERS).path(UserResource.TOKEN).post().build()
        );
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void testLoginNonMobile() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                restService.logout().restBuilder().basicAuth("1", "kk")
                        .path(UserResource.USERS).path(UserResource.TOKEN).post().build()
        );
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

}
