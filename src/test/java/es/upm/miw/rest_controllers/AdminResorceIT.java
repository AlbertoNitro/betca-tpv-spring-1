package es.upm.miw.rest_controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApiTestConfig
class AdminResourceIT {

    @Autowired
    private RestService restService;

    @Test
    void testDeleteDB() {
        this.restService.deleteDB();
    }

    @Test
    void testSeedDBUpload() {
        this.restService.reLoadTestDB();
    }

}
