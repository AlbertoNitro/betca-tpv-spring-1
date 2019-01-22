package es.upm.miw.restControllers;

import es.upm.miw.businessControllers.TicketController;
import es.upm.miw.dtos.TicketCreationInputDto;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.exceptions.PdfException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(TicketResource.TICKETS)
public class TicketResource {
    public static final String TICKETS = "/tickets";

    @Autowired
    private TicketController ticketController;

    @PostMapping(produces = {"application/pdf; charset=UTF-8", "application/json"})
    public byte[] createTicket(@Valid @RequestBody TicketCreationInputDto ticketCreationDto)
            throws NotFoundException, PdfException, BadRequestException {
        return this.ticketController.createTicketAndPdf(ticketCreationDto);
    }

}
