package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.ProviderController;
import es.upm.miw.dtos.ProviderMinimunDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(ProviderResource.PROVIDERS)
public class ProviderResource {

    public static final String PROVIDERS = "/providers";
    public static final String ACTIVES = "/actives";

    @Autowired
    private ProviderController providerController;

    @GetMapping
    public List<ProviderMinimunDto> readAll() {
        return this.providerController.readAll();
    }

    @GetMapping(value = ACTIVES)
    public List<ProviderMinimunDto> readAllActives() {
        return this.providerController.readAllActives();
    }
}
