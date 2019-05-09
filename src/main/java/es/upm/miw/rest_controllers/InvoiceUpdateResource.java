package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.InvoiceUpdateController;
import es.upm.miw.dtos.output.InvoiceUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasRole ('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(InvoiceUpdateResource.INVOICEUPDATE)
public class InvoiceUpdateResource {
    @Autowired
    private InvoiceUpdateController invoiceUpdateController;

    public static final String INVOICEUPDATE = "/invoice-update";
    @GetMapping()
    public List<InvoiceUpdateDto> getAll() {
        return null;
    }

}
