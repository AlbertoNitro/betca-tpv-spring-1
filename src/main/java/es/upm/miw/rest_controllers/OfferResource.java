package es.upm.miw.rest_controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(OfferResource.OFFERS)
public class OfferResource {

    public static final String OFFERS = "/offers";

    @GetMapping
    public String readAll() {
        return "Entra a /offers";
    }
}