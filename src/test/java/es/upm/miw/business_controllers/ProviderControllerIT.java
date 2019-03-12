package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.data_services.DatabaseSeederService;
import es.upm.miw.dtos.ProviderMinimunDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class ProviderControllerIT {

    @Autowired
    private ProviderController providerController;

    @Test
    void testReadAll() {
        List<ProviderMinimunDto> providers = providerController.readAll();
        assertTrue(providers.size() > 1);
    }
}
