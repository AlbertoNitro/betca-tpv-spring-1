package es.upm.miw.business_controllers;

import es.upm.miw.business_services.PdfService;
import es.upm.miw.documents.Invoice;
import es.upm.miw.documents.Ticket;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.input.TicketCreationInputDto;
import es.upm.miw.dtos.output.InvoiceDto;
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
public class InvoiceController {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private TicketController ticketController;

    private static final double TAX_RATE = 0.21;

    private Invoice convertInvoiceUpdateDtoToInvoice(InvoiceUpdateDto invoiceUpdateDto){
        Invoice invoice = new Invoice (
                invoiceUpdateDto.getBaseTax(),
                invoiceUpdateDto.getTax(),
                invoiceUpdateDto.getReferencesPositiveInvoice()
        );
        return invoice;
    }
    private Invoice convertInvoiceDtoToInvoice(InvoiceDto invoiceDto){
        Invoice invoice = new Invoice(
                invoiceDto.getBaseTax(),
                invoiceDto.getTax()
        );
        return invoice;
    }
    private Invoice ConvertTicketDtoToInvoice(TicketCreationInputDto ticketCreationInputDto)
    {
        BigDecimal total = new BigDecimal(0);
        BigDecimal baseTax = new BigDecimal(0);
        BigDecimal tax = new BigDecimal(0);

        total = total.add(ticketCreationInputDto.getCash());
        total = total.add(ticketCreationInputDto.getCard());
        total = total.add(ticketCreationInputDto.getVoucher());

        tax = total.multiply(new BigDecimal(TAX_RATE));
        baseTax = total.subtract(tax);
        Ticket ticket = ticketController.createTicket(ticketCreationInputDto);

        Invoice invoice = new Invoice(baseTax, tax, ticket);
        invoice.setUser(ticket.getUser());
        return invoice;
    }
    private Invoice ConvertTicketToInvoice(Ticket ticket)
    {
        BigDecimal total = new BigDecimal(0);
        total = total.add(ticket.getCash());
        total = total.add(ticket.getCard());
        total = total.add(ticket.getVoucher());

        BigDecimal tax = total.multiply(new BigDecimal(TAX_RATE));
        BigDecimal baseTax = total.subtract(tax);
        Invoice invoice = new Invoice(baseTax, tax, ticket);
        invoice.setUser(ticket.getUser());
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
        char discriminator = '-';
        char discriminated = date.charAt(4);
        if (discriminated == discriminator) {
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
    private boolean validateValidInvoiceUser(User user){
        if(user.getAddress() != null && !user.getAddress().trim().isEmpty()
                && user.getUsername()!= null && !user.getUsername().trim().isEmpty())
            return true;
        else
            return false;
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
        if (invoices.size()>0) {
            List<InvoiceUpdateDto> invoiceUpdateDtos = convertInvoiceToInvoiceUpdateDto(invoices);
            return invoiceUpdateDtos;
        } else
            return null;

    }
    public List<InvoiceUpdateDto> getInvoiceByMobileAndCreationDateBetween(String mobile,
                                                                           String afterDate,
                                                                           String beforeDate) {
        Optional<User> userOptional = userRepository.findByMobile(mobile);
        User user = userOptional.get();
        List<Invoice> invoices = invoiceRepository
                .findByUserAndCreationDateBetween(user, convertStringToLocalDateTime(afterDate),
                        convertStringToLocalDateTime(beforeDate));
        if (invoices.size()>0){
            return convertInvoiceToInvoiceUpdateDto(invoices);
        } else return new ArrayList<InvoiceUpdateDto>();
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
    public byte [] createInvoiceAndPdf(TicketCreationInputDto ticketCreationInputDto){
        Invoice invoice = ConvertTicketDtoToInvoice(ticketCreationInputDto);
        if(invoice != null){
            invoiceRepository.save(invoice);
            return  this.pdfService.generateInvoice(invoice,invoice.getTicket());
        }
        return null;
    }
    public byte [] generateInvoicePdfByTicketReference(String id){
        Ticket ticket = ticketRepository.findById(id).get();
        if (ticket == null)
            return null;
        if(!validateValidInvoiceUser(ticket.getUser()))
            return null;
        Invoice invoice = ConvertTicketToInvoice(ticket);
        if(invoice == null){
            return null;
        }else {
            invoiceRepository.save(invoice);
            return this.pdfService.generateInvoice(invoice,ticket);
        }
    }
}
