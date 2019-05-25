package es.upm.miw.business_controllers;

import es.upm.miw.documents.Invoice;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.output.InvoiceUpdateDto;
import es.upm.miw.repositories.InvoiceRepository;
import es.upm.miw.repositories.TicketRepository;
import es.upm.miw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class InvoiceUpdateController {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;

    private List<InvoiceUpdateDto> convertInvoiceToInvoiceUpdateDto(List<Invoice> invoices) {
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
    private LocalDateTime convertStringToLocalDateTime(String date) {
        System.out.println("Fecha: "+date);
        return LocalDateTime.parse(date);
    }
    public List<InvoiceUpdateDto> getAll() {
        List<Invoice> invoices;
        return convertInvoiceToInvoiceUpdateDto(this.invoiceRepository.findAll());
    }

    public List<InvoiceUpdateDto> getInvoiceByMobile(String mobile) {
        Optional<User> userOptional = userRepository.findByMobile(mobile);
        User user = userOptional.get();
        List<Invoice> invoices = invoiceRepository.findByUser(user);
        return convertInvoiceToInvoiceUpdateDto(invoices);
    }
    public List<InvoiceUpdateDto> getInvoiceByCreationDateAfter(String afterDate) {
        List<Invoice> invoices = invoiceRepository
                .findByCreationDateAfter(convertStringToLocalDateTime(afterDate));
        return convertInvoiceToInvoiceUpdateDto(invoices);
    }
    public List<InvoiceUpdateDto> getInvoiceByCreationDateBetween(String afterDate, String beforeDate) {
        List<Invoice> invoices = invoiceRepository
                .findByCreationDateBetween(convertStringToLocalDateTime(afterDate),
                                            convertStringToLocalDateTime(beforeDate));
        return convertInvoiceToInvoiceUpdateDto(invoices);
    }
    public List<InvoiceUpdateDto> getInvoiceByMobileAndCreationDateBetween(String mobile,
                                                                           String afterDate,
                                                                           String beforeDate) {
        Optional<User> userOptional = userRepository.findByMobile(mobile);
        User user = userOptional.get();
        List<Invoice> invoices = invoiceRepository
                .findByUserAndCreationDateBetween(user, convertStringToLocalDateTime(afterDate),
                        convertStringToLocalDateTime(beforeDate));
        return convertInvoiceToInvoiceUpdateDto(invoices);
    }
}
