package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Provider;
import es.upm.miw.dtos.ProviderMinimunDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class ProviderRepositoryIT {

    @Autowired
    private ProviderRepository providerRepository;
    private Provider inactive;
    private Provider active;

    @BeforeEach
    void seedDb() {
        this.active = new Provider("active-company");
        this.inactive = new Provider("inactive-company");
        this.inactive.setActive(false);
        this.providerRepository.save(inactive);
        this.providerRepository.save(active);
    }


    @Test
    void testFindById(){
        Optional<Provider> provider = this.providerRepository.findById(active.getId());
        assertTrue(provider.isPresent());
        assertTrue(provider.get().getCompany().equals("active-company"));
    }

    @Test
    void testFindByIdNotFound(){
        Optional<Provider> provider = this.providerRepository.findById("non-existent-id");
        assertFalse(provider.isPresent());
    }

    @Test
    void testReadAll() {
        assertTrue(this.providerRepository.findAll().size() > 1);
    }

    @Test
    void testFindAllProviders() {
        List<ProviderMinimunDto> providers = providerRepository.findAllProviders();
        assertTrue(providers.size() > 1);
    }

    @Test
    void testFindByActiveTrue() {
        List<ProviderMinimunDto> providers = providerRepository.findByActiveTrue();
        assertTrue(containsCompany(providers, "active-company"));
        assertFalse(containsCompany(providers, "inactive-company"));
    }

    private static boolean containsCompany(List<ProviderMinimunDto> providers, String company){
        return providers.stream().anyMatch(item -> company.equals(item.getCompany()));
    }

    @AfterEach
    void delete() {
        this.providerRepository.delete(inactive);
        this.providerRepository.delete(active);
    }

}