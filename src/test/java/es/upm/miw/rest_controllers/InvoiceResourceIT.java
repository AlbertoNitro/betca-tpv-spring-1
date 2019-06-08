package es.upm.miw.rest_controllers;

import es.upm.miw.documents.Ticket;
import es.upm.miw.dtos.ShoppingDto;
import es.upm.miw.dtos.input.TicketCreationInputDto;
import es.upm.miw.repositories.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ApiTestConfig
public class InvoiceResourceIT {
    @Autowired
    private RestService restService;

    @Autowired
    private TicketRepository ticketRepository;

    private List<Ticket> initialTicketDB;

    @BeforeEach
    void backupTicketDB() {
        initialTicketDB = this.ticketRepository.findAll();
    }

    @AfterEach
    void resetTicketDB() {
        this.ticketRepository.deleteAll();
        this.ticketRepository.saveAll(this.initialTicketDB);
    }

    @Test
    void testCreateTInvoice() {
        ShoppingDto shoppingDto =
                new ShoppingDto("1", "", new BigDecimal("100.00"), 1, BigDecimal.ZERO,
                        new BigDecimal("100.00"), true);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(null, new BigDecimal("100.00")
                , BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto), "Nota del ticket...", "Nota Regalo");
        byte[] pdf = this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(InvoiceResource.INVOICE).body(ticketCreationInputDto)
                .post().build();
        assertNotNull(pdf);
    }
}
