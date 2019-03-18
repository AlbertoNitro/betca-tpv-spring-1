package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.output.InfoOutputDto;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ApiTestConfig
class AdminResourceIT {

    @Autowired
    private RestService restService;

    @Test
    void testInfo(){
        InfoOutputDto info=this.restService.loginAdmin().restBuilder(new RestBuilder<InfoOutputDto>())
                .clazz(InfoOutputDto.class).path(AdminResource.ADMINS).path(AdminResource.INFO).get().build();
        assertNotNull(info.getName());
        assertNotNull(info.getVersion());
        assertNotNull(info.getDate());
    }

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
    void testSeedDBUploadError() {
        assertThrows(HttpClientErrorException.NotFound.class, () ->
                this.restService.loginAdmin().restBuilder().loadFile("testEmpty.yml").path(AdminResource.ADMINS)
                        .path(AdminResource.DB).post().log().build());
    }

    @Test
    void testSeedDBUploadErrorNotException() throws IOException {
                String json = this.restService.loginAdmin().restBuilder(new RestBuilder<String>()).clazz(String.class)
                        .loadFile("testEmpty.yml").path(AdminResource.ADMINS).path(AdminResource.DB)
                        .post().log().notError().build();
        LogManager.getLogger(this.getClass()).info("Error Message: " + json);
    }

    @Test
    void testSeedDB() {
        this.restService.deleteDB();
        this.restService.loginAdmin().restBuilder().path(AdminResource.ADMINS)
                .path(AdminResource.DB).post().build();
        this.restService.reLoadTestDB();
    }

}
