package es.upm.miw.rest_controllers;

import es.upm.miw.documents.User;
import es.upm.miw.dtos.ProviderDto;
import es.upm.miw.dtos.ProviderMinimunDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
public class ProviderResourceIT {

    @Autowired
    private RestService restService;

    @Test
    void testReadNotFound() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder(new RestBuilder<ProviderMinimunDto[]>()).clazz(ProviderMinimunDto[].class)
                .path(ProviderResource.PROVIDERS).path("/non-existent-id")
                .get().build());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testRead() {
        List<ProviderMinimunDto> providers = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<ProviderMinimunDto[]>()).clazz(ProviderMinimunDto[].class)
                .path(ProviderResource.PROVIDERS)
                .get().build());
        String existentId = providers.get(0).getId();
        ProviderDto providerDto= this.restService.loginAdmin()
                .restBuilder(new RestBuilder<ProviderDto>()).clazz(ProviderDto.class)
                .path(ProviderResource.PROVIDERS).path("/" +  existentId)
                .get().build();
    }

    @Test
    void testReadAll() {
        List<ProviderMinimunDto> providers = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<ProviderMinimunDto[]>()).clazz(ProviderMinimunDto[].class)
                .path(ProviderResource.PROVIDERS)
                .get().build());
        assertTrue(providers.size() > 1);
    }

    @Test
    void testRealAllActives() {
        List<ProviderMinimunDto> actives = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<ProviderMinimunDto[]>()).clazz(ProviderMinimunDto[].class)
                .path(ProviderResource.PROVIDERS).path(ProviderResource.ACTIVES)
                .get().build());
        assertTrue(actives.size() > 1);
    }
}
