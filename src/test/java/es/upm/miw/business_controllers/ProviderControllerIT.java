package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.data_services.DatabaseSeederService;
import es.upm.miw.documents.Provider;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.ProviderDto;
import es.upm.miw.dtos.ProviderMinimunDto;
import es.upm.miw.exceptions.ConflictException;
import es.upm.miw.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void testRealAllActives(){
        List<ProviderMinimunDto> providers = providerController.readAllActives();
        assertTrue(providers.size() > 1);
    }

    @Test
    void testRead(){
        String existentId = providerController.readAll().get(0).getId();
        ProviderDto provider = providerController.read(existentId);
    }

    @Test
    void testReadNotFound(){
        assertThrows(NotFoundException.class, () -> providerController.read("non-existent-id"));
    }

    @Test
    void testCreate() {
        ProviderDto providerDto = new ProviderDto("my-company");
        this.providerController.create(providerDto);
        assertThrows(ConflictException.class, () -> this.providerController.create(providerDto));
    }
}
