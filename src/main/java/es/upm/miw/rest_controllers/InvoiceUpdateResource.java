package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.InvoiceUpdateController;
import es.upm.miw.dtos.output.InvoiceUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@PreAuthorize("hasRole ('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(InvoiceUpdateResource.INVOICEUPDATE)
public class InvoiceUpdateResource {
    public static final String MOBILEID = "/mobile/{mobile}";
    public static final String FROMDATE = "/dates/{fromdate}";
    public static final String BETWEENDATES = "/dates/{afterDate}/{beforeDate}";
    public static final String MOBILEIDBETWEENDATES = "/dates/{mobile}/{afterDate}/{beforeDate}";
    public static final String INVOICEUPDATE = "/invoice-update";
    public static final String PDF = "/pdf/{id}";
    @Autowired
    private InvoiceUpdateController invoiceUpdateController;

    @GetMapping()
    public List<InvoiceUpdateDto> getAll() {
        return invoiceUpdateController.getAll();
    }

    @GetMapping(value = MOBILEID)
    public List<InvoiceUpdateDto> getInvoicesByMobile(@PathVariable String mobile) {
        return invoiceUpdateController.getInvoiceByMobile(mobile);
    }
    @RequestMapping(value = PDF, produces = {"application/pdf"}, method=GET)
    public byte[] getInvoicePDF(@PathVariable String id){
        byte[] response = invoiceUpdateController.generatePdf(id);
        return response;
    }
    @GetMapping(value = FROMDATE)
    public List<InvoiceUpdateDto> getInvoicesByCreationDateAfter(@PathVariable String afterDate) {
        return invoiceUpdateController.getInvoiceByCreationDateAfter(afterDate);
    }
    @GetMapping(value = BETWEENDATES)
    public List<InvoiceUpdateDto> getInvoicesByCreationDateBetween(@PathVariable String afterDate, @PathVariable String beforeDate) {
            return invoiceUpdateController.getInvoiceByCreationDateBetween(afterDate, beforeDate);
    }
    @GetMapping(value = MOBILEIDBETWEENDATES)
    public List<InvoiceUpdateDto> getInvoicesByMobileAndCreationDateBetween(@PathVariable String mobile,
                                                                   @PathVariable String afterDate,
                                                                   @PathVariable String beforeDate) {
        return invoiceUpdateController.getInvoiceByMobileAndCreationDateBetween(mobile,
                                                                                afterDate,
                                                                                beforeDate);
    }

}
