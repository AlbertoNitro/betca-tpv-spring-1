package es.upm.miw.business_controllers;

import es.upm.miw.documents.Invoice;
import es.upm.miw.dtos.output.InvoiceUpdateDto;
import es.upm.miw.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
@Controller
public class InvoiceUpdateController {
    @Autowired
    private InvoiceRepository invoiceRepository;

    private List<InvoiceUpdateDto> convertInvoiceToInvoiceUpdateDto (List<Invoice> invoices){
        InvoiceUpdateDto invoiceUpdateDto;
        List<InvoiceUpdateDto> invoiceUpdateDtoList = new ArrayList<InvoiceUpdateDto>();
        for (Invoice invoice : invoices) {
            invoiceUpdateDto = new InvoiceUpdateDto(
                    invoice.getId(),
                    invoice.getCreationDated().toString(),
                    Float.parseFloat(invoice.getBaseTax().toString()),
                    Float.parseFloat(invoice.getTax().toString())
            );
            invoiceUpdateDtoList.add(invoiceUpdateDto);
        }
        return invoiceUpdateDtoList;
    }

    public List<InvoiceUpdateDto> getAll() {
        List<Invoice> invoices;
        return convertInvoiceToInvoiceUpdateDto( this.invoiceRepository.findAll());
    }

}
