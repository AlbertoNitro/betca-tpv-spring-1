package es.upm.miw.rest_controllers;

import es.upm.miw.exceptions.JwtException;
import es.upm.miw.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ApiTestConfig
class AdminResourceIT {

    @Autowired
    private RestService restService;

    @Test
    void testDeleteDB() {
        this.restService.deleteDB();
        this.restService.reLoadTestDB();
    }

    @Test
    void testSeedDBUpload() throws IOException {
        this.restService.loginAdmin().restBuilder().loadFile("test.yml").path(AdminResource.ADMINS)
                .path(AdminResource.DB).post().log().build();
        this.restService.reLoadTestDB();
    }

    @Test
    void testSeedDBUploadError(){
        assertThrows(HttpClientErrorException.NotFound.class, () ->
                this.restService.loginAdmin().restBuilder().loadFile("testEmpty.yml").path(AdminResource.ADMINS)
                .path(AdminResource.DB).post().log().build());
    }

    @Test
    void testSeedDB() {
        this.restService.deleteDB();
        this.restService.loginAdmin().restBuilder().path(AdminResource.ADMINS)
                .path(AdminResource.DB).post().build();
        this.restService.reLoadTestDB();
    }

}
