package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.ProviderMinimunDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ApiTestConfig
public class ProviderResourceIT {

    @Autowired
    private RestService restService;

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
