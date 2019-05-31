package es.upm.miw.business_controllers;

import es.upm.miw.business_services.PdfService;
import es.upm.miw.documents.Invoice;
import es.upm.miw.documents.Ticket;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.output.InvoiceUpdateDto;
import es.upm.miw.repositories.InvoiceRepository;
import es.upm.miw.repositories.TicketRepository;
import es.upm.miw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Controller
public class InvoiceUpdateController {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private PdfService pdfService;
    private Invoice convertInvoiceUpdateDtoToInvoice(InvoiceUpdateDto invoiceUpdateDto){
        Invoice invoice = new Invoice (
                invoiceUpdateDto.getBaseTax(),
                invoiceUpdateDto.getTax(),
                invoiceUpdateDto.getReferencesPositiveInvoice()
        );
        return invoice;
    }
    private List<InvoiceUpdateDto> convertInvoiceToInvoiceUpdateDto(List<Invoice> invoices) {
        InvoiceUpdateDto invoiceUpdateDto;
        List<InvoiceUpdateDto> invoiceUpdateDtoList = new ArrayList<InvoiceUpdateDto>();
        for (Invoice invoice : invoices) {
            invoiceUpdateDto = new InvoiceUpdateDto(
                    invoice.getId(),
                    invoice.getCreationDate().toString(),
                    invoice.getBaseTax(),
                    invoice.getTax(),
                    invoice.getReferencesPositiveInvoice(),
                    new BigDecimal(0)
            );
            invoiceUpdateDtoList.add(invoiceUpdateDto);
        }
        return invoiceUpdateDtoList;
    }
    private LocalDateTime convertStringToLocalDateTime(String date) {
        SimpleDateFormat simpleDateFormat;
        char discriminator = date.charAt(4);
        if ("-".equals(discriminator)) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }else {
            simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        }
            Date convertedDate = null;
        try{
            convertedDate = simpleDateFormat.parse(date);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

    }
    public List<InvoiceUpdateDto> getAll() {
        return convertInvoiceToInvoiceUpdateDto(this.invoiceRepository.findAll());
    }
    public List<InvoiceUpdateDto> getInvoiceByMobile(String mobile) {
        Optional<User> userOptional = userRepository.findByMobile(mobile);
        User user = userOptional.get();
        List<Invoice> invoices = invoiceRepository.findByUser(user);

        List<InvoiceUpdateDto> invoiceUpdateDtos = convertInvoiceToInvoiceUpdateDto(invoices);
        return invoiceUpdateDtos;
    }
    public List<InvoiceUpdateDto> getInvoiceByCreationDateBetween(String afterDateString, String beforeDateString) {
        LocalDateTime afterDate = convertStringToLocalDateTime(afterDateString);
        LocalDateTime beforeDate = convertStringToLocalDateTime(beforeDateString);
        List<Invoice> invoices = invoiceRepository
                .findByCreationDateBetween(afterDate, beforeDate);
        List<InvoiceUpdateDto> invoiceUpdateDtos = convertInvoiceToInvoiceUpdateDto(invoices);
        System.out.println("La lista de invoices: " + invoices + " - la lista de DTO: " + invoiceUpdateDtos);
        return invoiceUpdateDtos;

    }
    public List<InvoiceUpdateDto> getInvoiceByMobileAndCreationDateBetween(String mobile,
                                                                           String afterDate,
                                                                           String beforeDate) {
        Optional<User> userOptional = userRepository.findByMobile(mobile);
        User user = userOptional.get();
        System.out.println("USER: " + user.getId().toString());
        System.out.println("afterD: " + convertStringToLocalDateTime(afterDate));
        System.out.println("beforeDate: " + convertStringToLocalDateTime(beforeDate));
        System.out.println(user);
        List<Invoice> invoices = invoiceRepository
                .findByUserAndCreationDateBetween(user, convertStringToLocalDateTime(afterDate),
                        convertStringToLocalDateTime(beforeDate));
        return convertInvoiceToInvoiceUpdateDto(invoices);
    }
    public byte[] generatePdf(String id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        Optional<Ticket> ticket = ticketRepository.findById(invoice.get().getTicket().getId());
        return this.pdfService.generateInvoice(invoice.get(), ticket.get());
    }
    public BigDecimal look4PosibleTotal (String id) {
        BigDecimal posibleTotal = new BigDecimal(0);
        BigDecimal ZERO = new BigDecimal(0);
        Optional<List<Invoice>> negativeinvoices = null;
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        Invoice positiveInvoice;
        if (invoice.isPresent()
                && invoice.get().getTicket().getCash().compareTo(ZERO) >= 0
                && invoice.get().getTicket().getCard().compareTo(ZERO) >= 0){
            positiveInvoice = invoice.get();
            posibleTotal = new BigDecimal(String.valueOf(positiveInvoice.getTicket().getCash()));
            posibleTotal = posibleTotal.add(positiveInvoice.getTicket().getCard());
            negativeinvoices = Optional.ofNullable(invoiceRepository
                    .findByReferencesPositiveInvoice(positiveInvoice.getId()));
            if (negativeinvoices.isPresent()) {
                BigDecimal cash;
                BigDecimal card;
                for (Invoice negativeinvoice : negativeinvoices.get()) {
                    cash = negativeinvoice.getTicket().getCard();
                    card = negativeinvoice.getTicket().getCash();
                    posibleTotal = posibleTotal.add(cash);
                    posibleTotal = posibleTotal.add(card);
                }
            }
        }

        return posibleTotal;
    }
    public byte [] createNegativeInvoiceAndPdf(InvoiceUpdateDto invoiceUpdateDto){
        Invoice negativeInvoice = convertInvoiceUpdateDtoToInvoice(invoiceUpdateDto);
        Optional<Ticket> oldTicket = Optional.ofNullable(invoiceRepository.findById(invoiceUpdateDto.getId()).get().getTicket());
        Ticket negativeTicket = null;
        if (oldTicket.isPresent()) {
            BigDecimal oldCash = oldTicket.get().getCash();
            BigDecimal oldCard = oldTicket.get().getCard();
            BigDecimal negativeCash = new BigDecimal(0);
            BigDecimal negativeCard = new BigDecimal(0);
            BigDecimal differenceCashCard = new BigDecimal(0);
            if (invoiceUpdateDto.getNegative().compareTo(oldCash) > 0 ) {
                differenceCashCard = invoiceUpdateDto.getNegative().subtract(oldCash);
                negativeCard = differenceCashCard.negate();
            }
            negativeCash = invoiceUpdateDto.getNegative().subtract(differenceCashCard).negate();
            negativeTicket = new Ticket(1,
                                        negativeCard,
                                        negativeCash,
                                        new BigDecimal(0),
                                        oldTicket.get().getShoppingList(),
                                        oldTicket.get().getUser());
            ticketRepository.save(negativeTicket);
            negativeInvoice.setTicket(negativeTicket);
            negativeInvoice.setUser(oldTicket.get().getUser());
        }
        if (negativeInvoice != null) {
            invoiceRepository.save(negativeInvoice);
            return this.pdfService.generateInvoice(negativeInvoice, negativeInvoice.getTicket());
        }
        return null;
    }
}
