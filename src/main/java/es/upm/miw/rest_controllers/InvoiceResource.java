package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.InvoiceController;
import es.upm.miw.dtos.input.TicketCreationInputDto;
import es.upm.miw.dtos.output.InvoiceUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasRole ('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(InvoiceResource.INVOICE)
public class InvoiceResource {
    public static final String INVOICE = "/invoice";
    public static final String INVOICE_GENERATE = "/generate-invoice";
    public static final String TICKET_REFERENCE = "/{ticketReference}";

    @Autowired
    private InvoiceController invoiceController;
    @PostMapping(produces = {"application/pdf", "application/json"})
    public byte[] createInvoice(@Valid @RequestBody TicketCreationInputDto ticketCreationDto){
        byte[] response = this.invoiceController.createInvoiceAndPdf(ticketCreationDto);
        return response;
    }
    @GetMapping(value = INVOICE_GENERATE + TICKET_REFERENCE)
    public byte[] generateInvoicePdfByTicketReference(@PathVariable String ticketReference) {
        return invoiceController.generateInvoicePdfByTicketReference(ticketReference);
    }
}
