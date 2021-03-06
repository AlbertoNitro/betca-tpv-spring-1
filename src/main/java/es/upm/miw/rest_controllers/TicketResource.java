package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.TicketController;
import es.upm.miw.documents.Article;
import es.upm.miw.dtos.TicketModificationStateOrAmountDto;
import es.upm.miw.dtos.input.TicketCreationInputDto;
import es.upm.miw.dtos.input.TicketQueryInputDto;
import es.upm.miw.dtos.output.TicketQueryOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(TicketResource.TICKETS)
public class TicketResource {
    public static final String TICKETS = "/tickets";
    public static final String GIFT = "/gift";
    public static final String QUERY = "/query";
    public static final String ORDER_ID = "/orderId";
    public static final String TICKET_ID = "/{id}";
    public static final String DATE_SOLD = "/datesold/{datesold}";

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

    @GetMapping(value = TICKET_ID)
    public TicketModificationStateOrAmountDto obtainTicketModifiedById(@PathVariable String id) {
        return ticketController.obtainTicketModifiedById(id);
    }

    @PutMapping(value = TICKET_ID, produces = {"application/pdf", "application/json"})
    public byte[] updateModifiedTicketAndPdf(
            @PathVariable String id
            , @Valid @RequestBody TicketModificationStateOrAmountDto modifiedTicket) {
        return ticketController.updateModifiedTicketAndPdf(id, modifiedTicket);
    }

    @GetMapping(value = GIFT, produces = {"application/pdf", "application/json"})
    public byte[] generateGiftTicketResource(@Valid @RequestParam(required = false) String id) {
        return ticketController.generateGiftTicketController(id);
    }

    @GetMapping(value = DATE_SOLD)
    public List<Article> getDateSold(@PathVariable String datesold) {
        return this.ticketController.getDateSold(datesold);
    }

}
