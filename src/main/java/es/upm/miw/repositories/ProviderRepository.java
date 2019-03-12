package es.upm.miw.repositories;

import es.upm.miw.documents.Provider;
import es.upm.miw.dtos.ProviderMinimunDto;
import es.upm.miw.dtos.UserMinimumDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProviderRepository extends MongoRepository<Provider, String> {

    @Query(value = "{}", fields = "{ 'company' : 1, 'nif' : 1}")
    List<ProviderMinimunDto> findAllProviders();

}
