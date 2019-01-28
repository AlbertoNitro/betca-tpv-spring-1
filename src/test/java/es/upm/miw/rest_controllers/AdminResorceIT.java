package es.upm.miw.rest_controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

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
    void testSeedDB() {
        this.restService.deleteDB();
        this.restService.loginAdmin().restBuilder().path(AdminResource.ADMINS)
                .path(AdminResource.DB).post().log().build();
        this.restService.reLoadTestDB();
    }

}
