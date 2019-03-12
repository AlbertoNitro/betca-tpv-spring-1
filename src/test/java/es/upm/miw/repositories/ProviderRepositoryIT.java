package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.dtos.ProviderMinimunDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class ProviderRepositoryIT {

    @Autowired
    private ProviderRepository providerRepository;

    @Test
    void testReadAll() {
        assertTrue(this.providerRepository.findAll().size() > 1);
    }

    @Test
    void testFindAllProviders() {
        List<ProviderMinimunDto> providers = providerRepository.findAllProviders();
        System.out.println(providers);
        assertTrue(providers.size() > 1);
    }

}
