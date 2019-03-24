package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.input.OfferInputDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(OfferResource.OFFERS)
public class OfferResource {

    public static final String OFFERS = "/offers";

    @GetMapping
    public String readAll() {
        return "Entra a /offers";
    }

    @PostMapping
    public OfferInputDto createOffer(@Valid @RequestBody OfferInputDto offerInputDto) {
        return offerInputDto;
    }


}