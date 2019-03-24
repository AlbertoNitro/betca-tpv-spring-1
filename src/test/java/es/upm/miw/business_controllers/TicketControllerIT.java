package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.business_services.PdfService;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Shopping;
import es.upm.miw.documents.Ticket;
import es.upm.miw.dtos.ShoppingModificationStateOrAmountDto;
import es.upm.miw.dtos.TicketModificationStateOrAmountDto;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class TicketControllerIT {

    @Autowired
    private TicketController ticketController;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private PdfService pdfService;

    private TicketModificationStateOrAmountDto ticketModificationStateOrAmountDto;
    private Article article;
    private Shopping shopping;
    private Ticket ticket;
    private Shopping[] shoppings;
    private ShoppingModificationStateOrAmountDto modifiedShopping;
    private ShoppingModificationStateOrAmountDto[] modifiedShoppings;

    @BeforeEach
    void seed() {
        this.article = new Article();
        this.article.setCode("112287");
        this.articleRepository.save(this.article);
        this.shopping = new Shopping();
        this.shopping.setArticle(this.article);
        this.shopping.setAmount(1);
        this.shopping.setRetailPrice(BigDecimal.ONE);
        this.shopping.setDiscount(BigDecimal.ZERO);
        this.shoppings = new Shopping[1];
        this.shoppings[0] = this.shopping;
        this.ticket = new Ticket();
        this.ticket.setId("1395");
        this.ticket.setShoppingList(this.shoppings);
        this.ticketRepository.save(this.ticket);
        this.ticketModificationStateOrAmountDto = new TicketModificationStateOrAmountDto(this.ticket);
        this.modifiedShopping = new ShoppingModificationStateOrAmountDto(this.shopping);
        this.modifiedShoppings = new ShoppingModificationStateOrAmountDto[1];
        this.modifiedShoppings[0] = this.modifiedShopping;
        this.ticketModificationStateOrAmountDto.setShoppingList(this.modifiedShoppings);
    }

    @Test
    void testReadTicketById(){
        TicketModificationStateOrAmountDto ticketFound = ticketController.readTicketById("1395");
        assertNotNull(ticketFound);
        assertThrows(NotFoundException.class, () -> ticketController.readTicketById("Not exists"));

    }

/*    @Test
    void testCreateModifiedTicketAndPdf() {
        System.out.println(this.ticketModificationStateOrAmountDto.toString());
        assertNotNull(ticketController.createModifiedTicketAndPdf(this.ticketModificationStateOrAmountDto));
    }*/

}
