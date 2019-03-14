package es.upm.miw.business_controllers;

import es.upm.miw.documents.Provider;
import es.upm.miw.dtos.ProviderDto;
import es.upm.miw.dtos.ProviderMinimunDto;
import es.upm.miw.exceptions.ConflictException;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProviderController {

    @Autowired
    private ProviderRepository providerRepository;

    public ProviderDto read(String id){
        return new ProviderDto(this.providerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Provider id(" + id+ ")")));
    }

    public List<ProviderMinimunDto> readAll() {
        return this.providerRepository.findAllProviders();
    }

    public List<ProviderMinimunDto> readAllActives() {
        return this.providerRepository.findByActiveTrue();
    }

    public ProviderDto create(ProviderDto providerDto) {
        String company = providerDto.getCompany();
        if (this.providerRepository.findByCompany(company).isPresent())
            throw new ConflictException("Provider company (" + company + ")");
        Provider provider = new Provider(providerDto);
        this.providerRepository.save(provider);
        return new ProviderDto(provider);
    }

}
