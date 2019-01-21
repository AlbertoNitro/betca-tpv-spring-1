package es.upm.miw.restControllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApiTestConfig
class AdminResourceIT {

    @Autowired
    private RestService restService;

    @Test
    void testDeleteDB() {
        this.restService.loginAdmin().restBuilder()
                .path(AdminResource.ADMINS).path(AdminResource.DB)
                .delete().build();
        this.restService.reLoadTestDB();
    }

    @Test
    void testSeedDB() {
        this.restService.deleteDB();
        this.restService.loginAdmin().restBuilder()
                .path(AdminResource.ADMINS).path(AdminResource.DB).body("test.yml")
                .post().build();
    }


}
