package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.TicketController;
import es.upm.miw.dtos.input.TicketCreationInputDto;
import es.upm.miw.dtos.input.TicketQueryInputDto;
import es.upm.miw.dtos.output.TicketQueryOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(TicketResource.TICKETS)
public class TicketResource {
    public static final String TICKETS = "/tickets";
    public static final String QUERY = "/query";
    public static final String ORDER_ID = "/orderId";

    @Autowired
    private TicketController ticketController;

    @PostMapping(produces = {"application/pdf", "application/json"})
    public byte[] createTicket(@Valid @RequestBody TicketCreationInputDto ticketCreationDto) {
        return this.ticketController.createTicketAndPdf(ticketCreationDto);
    }

    @PostMapping(value = QUERY)
    public List<TicketQueryOutputDto> advancedTicketQuery(@RequestBody TicketQueryInputDto ticketQueryDto) {
        return this.ticketController.advancedTicketQuery(ticketQueryDto);
    }

    @PostMapping(value = QUERY+ORDER_ID)
    public List<TicketQueryOutputDto> advancedTicketQueryByOrderId(@RequestBody TicketQueryInputDto ticketQueryDto) {
        return this.ticketController.advancedTicketQueryByOrderId(ticketQueryDto);
    }

}
