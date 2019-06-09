package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.*;
import es.upm.miw.dtos.ShoppingDto;
import es.upm.miw.dtos.input.TicketCreationInputDto;
import es.upm.miw.dtos.output.InvoiceUpdateDto;
import es.upm.miw.repositories.InvoiceRepository;
import es.upm.miw.repositories.TicketRepository;
import es.upm.miw.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class InvoiceControllerIT {

    @Autowired
    private InvoiceController invoiceController;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    private Invoice invoice;
    private List<Invoice> invoices;
    private Ticket ticket;
    private User user;
    private ShoppingDto[] shoppingsDto;
    private Shopping[] shoppings;

    @BeforeEach
    void seedDb() {
        userRepository.deleteByMobile("777");
        this.shoppings = new Shopping[2];
        Shopping shopping = new Shopping(1, new BigDecimal(1), Article.builder("1").retailPrice("20")
                .description("Varios").build());
        Shopping shopping2 = new Shopping(2, new BigDecimal(1), Article.builder("1").retailPrice("5")
                .description("Varios2").build());
        this.shoppings[0] = shopping;
        this.shoppings[1] = shopping2;
        this.shoppingsDto = new ShoppingDto[2];
        ShoppingDto shoppingDto = new ShoppingDto(shopping);
        ShoppingDto shoppingDto2 = new ShoppingDto(shopping2);
        this.shoppingsDto[0] = shoppingDto;
        this.shoppingsDto[1] = shoppingDto2;
        this.user =  new User("777", "User777", "777", "123456789",
                "10", "C/ TPV 5, Vallekas", "777@mail.com");
        this.userRepository.save(user);
        this.ticket = new Ticket(5, new BigDecimal(25), new BigDecimal(100), new BigDecimal(0),
                shoppings, user);
        this.ticketRepository.save(ticket);
        this.invoice = new Invoice(new BigDecimal(10), new BigDecimal(15), ticket, ticket.getReference());
        this.invoice.setId("11");
        this.invoice.setUser(user);
        this.invoice.setTicket(ticket);
        this.invoiceRepository.save(invoice);
    }

    @Test
    void testReadAll() {
        List<InvoiceUpdateDto> invoices = invoiceController.getAll();
        // System.out.println(invoices);
        assertTrue(invoices.size() > 0);
    }
    @Test
    void generatePdf() {
        this.invoices = invoiceRepository.findAll();
        byte[] invoicePdf = invoiceController.generatePdf(invoices.get(0).getId());
        assertNotNull(invoicePdf);
    }
    @Test
    void getInvoiceByMobile() {
        List<InvoiceUpdateDto> testInvoiceDtoList = invoiceController.getInvoiceByMobile("777");
        LocalDateTime creationDateTest = invoiceRepository.findAll().get(0).getCreationDate();
        assertEquals(this.user.getMobile(), "777");
    }

    @Test
    void getInvoiceByMobileAndCreationDateBetween() {
        String afterDateTest = LocalDateTime.now().minusDays(5).toString();
        String beforeDateTest = LocalDateTime.now().plusDays(5).toString();
        List<InvoiceUpdateDto> testInvoiceDtoList = invoiceController
                .getInvoiceByMobileAndCreationDateBetween("666666004", afterDateTest, beforeDateTest);
        User userActual = invoiceRepository.findAll().get(0).getUser();
        User userExpected = userRepository.findByMobile("666666004").get();
        assertEquals(userActual, userExpected );
    }
    @Test
    void look4PosibleTotal() {
        String idTest = invoiceController.getInvoiceByMobile("777").get(0).getId();
        BigDecimal expectedPosibleTotal = new BigDecimal(125);
        assertEquals(expectedPosibleTotal, new BigDecimal(String.valueOf(invoiceController.look4PosibleTotal(idTest))));
    }
    @Test
    void createNegativeInvoiceAndPdf() {
        int invoicesSize = invoiceRepository.findAll().size();
        String testInvoiceId = invoiceRepository.findAll().get(0).getId();
        InvoiceUpdateDto invoiceUpdateDtoTest = new InvoiceUpdateDto("11",
                                                                    LocalDateTime.now().toString(),
                                                                    new BigDecimal(10),
                                                                    new BigDecimal(12),
                                                                    testInvoiceId,
                                                                    new BigDecimal(100));

        byte[] invoiceNegativePdf = invoiceController.createNegativeInvoiceAndPdf(invoiceUpdateDtoTest);
        int invoicesSizePlusNegative = invoiceRepository.findAll().size();
        assertNotNull(invoiceNegativePdf);
        assertEquals(invoicesSize + 1, invoicesSizePlusNegative);
    }
    @Test
    void createInvoiceAndPdf(){

        List<ShoppingDto>lstShoppings = new ArrayList();
        lstShoppings.add(shoppingsDto[0]);
        lstShoppings.add(shoppingsDto[1]);
        int invoicesSize = invoiceRepository.findAll().size();
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(
                this.user.getMobile(),
                new BigDecimal(200),
                new BigDecimal(100),
                new BigDecimal(0),
                lstShoppings,
                "Test",
                ""
        );
        byte[] invoicePdf = invoiceController.createInvoiceAndPdf(ticketCreationInputDto);
        int invoicesSizePlusNewInvoice = invoiceRepository.findAll().size();
        assertNotNull(invoicePdf);
        assertEquals(invoicesSize + 1, invoicesSizePlusNewInvoice);
    }
    @Test
    void generateInvoicePDF(){
        byte[] invoicePdf = invoiceController.generateInvoicePdfByTicketReference("t2j_u9M9CisFmYGRFs1Uulgn7hI");
        assertNotNull(invoicePdf);
    }

}
