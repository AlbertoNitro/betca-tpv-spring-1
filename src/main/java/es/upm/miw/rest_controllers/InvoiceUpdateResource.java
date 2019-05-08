package es.upm.miw.rest_controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasRole ('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(InvoiceUpdateResource.INVOICEUPDATE)
public class InvoiceUpdateResource {
    public static final String INVOICEUPDATE = "/invoice-update";



}
