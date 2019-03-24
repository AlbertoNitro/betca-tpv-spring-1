package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.OfferController;
import es.upm.miw.dtos.input.OfferInputDto;
import es.upm.miw.dtos.output.OfferOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(OfferResource.OFFERS)
public class OfferResource {

    public static final String OFFERS = "/offers";
    public static final String OFFER_ID = "/{idOffer}";

    @Autowired
    private OfferController offerController;

    @GetMapping
    public List<OfferOutputDto> readAll() {
        return this.offerController.readAll();
    }

    @PostMapping
    public OfferOutputDto create(@Valid @RequestBody OfferInputDto offerInputDto) {
        return this.offerController.create(offerInputDto);
    }

    @DeleteMapping(value = OFFER_ID)
    public void delete(@PathVariable String idOffer) {
        this.offerController.delete(idOffer);
    }

}