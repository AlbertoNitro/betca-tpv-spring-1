package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.business_services.PdfService;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Shopping;
import es.upm.miw.documents.ShoppingState;
import es.upm.miw.documents.Ticket;
import es.upm.miw.dtos.ShoppingModificationStateOrAmountDto;
import es.upm.miw.dtos.TicketModificationStateOrAmountDto;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class TicketControllerIT {

    @Autowired
    private TicketController ticketController;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PdfService pdfService;

    private TicketModificationStateOrAmountDto ticketModificationStateOrAmountDto;
    private Ticket ticket;
    private Shopping[] shoppings;
    private ShoppingModificationStateOrAmountDto modifiedShopping;
    private List<ShoppingModificationStateOrAmountDto> modifiedShoppings;

    @BeforeEach
    void seed() {

        this.shoppings = new Shopping[1];
        this.shoppings[0] = this.createTestShopping();
        this.ticket = new Ticket();
        this.ticket.setId("1395");
        this.ticket.setShoppingList(this.shoppings);
        this.ticketRepository.save(this.ticket);
        this.ticketModificationStateOrAmountDto = new TicketModificationStateOrAmountDto(this.ticket, false);
        this.ticketModificationStateOrAmountDto.setId("1395");
        this.modifiedShopping = new ShoppingModificationStateOrAmountDto(this.shoppings[0]);
        this.modifiedShoppings = new ArrayList<>();
        this.modifiedShoppings.add(this.modifiedShopping);
        this.ticketModificationStateOrAmountDto.setShoppingList(this.modifiedShoppings);
    }

    private Shopping createTestShopping(){
        Shopping shopping = new Shopping();
        shopping.setAmount(1);
        shopping.setRetailPrice(BigDecimal.ONE);
        shopping.setDiscount(BigDecimal.ZERO);
        shopping.setShoppingState(ShoppingState.NOT_COMMITTED);
        shopping.setDescription("Test");
        return shopping;
    }

    @Test
    void testObtainTicketModifiedById(){
        TicketModificationStateOrAmountDto ticketFound = ticketController.obtainTicketModifiedById("1395");
        assertNotNull(ticketFound);
        assertThrows(NotFoundException.class, () -> ticketController.readTicketById("Not exists"));

    }

    @Test
    void testReadTicketById(){
        Ticket ticketFound = ticketController.readTicketById("1395");
        assertNotNull(ticketFound);
        assertThrows(NotFoundException.class, () -> ticketController.readTicketById("Not exists"));

    }

    @Test
    void updateModifiedTicketTest() {
        this.ticketModificationStateOrAmountDto.getShoppingList().get(0).setAmount(2);
        this.ticketModificationStateOrAmountDto.getShoppingList().get(0).setShoppingState(ShoppingState.IN_STOCK);
        Ticket ticket = ticketController.updateModifiedTicket("1395", this.ticketModificationStateOrAmountDto);
        assertEquals(ticket.getShoppingList()[0].getAmount()
                , this.ticketModificationStateOrAmountDto.getShoppingList().get(0).getAmount());
        assertEquals(ticket.getShoppingList()[0].getShoppingState()
                , this.ticketModificationStateOrAmountDto.getShoppingList().get(0).getShoppingState());
    }

    @Test
    void updateModifiedTicketAndPdf(){
        assertNotNull(ticketController.updateModifiedTicketAndPdf("1395", this.ticketModificationStateOrAmountDto));
    }

    @Test
    void testConvertStringToDate() {
        LocalDateTime correctDay =
                LocalDateTime.of(2019, 05, 30, 00, 00, 00);
        LocalDateTime incorrectDay =
                LocalDateTime.of(2018, 05, 30, 00, 00, 00);
        LocalDateTime result = ticketController.convertStringToDate("2019-05-30T00:00:00");
        assertEquals(correctDay, result);
        assertNotEquals(incorrectDay, result);
    }

    @Test
    void testGetDateSold(){
        List<Article> articles = this.ticketController.getDateSold("2019-05-30T00:00:00");
        assertNotNull(articles);
        assertTrue(articles.size() > 0);
    }
}
