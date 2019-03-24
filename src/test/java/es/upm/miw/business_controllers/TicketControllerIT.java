package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.data_services.DatabaseGraph;
import es.upm.miw.data_services.DatabaseSeederService;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Shopping;
import es.upm.miw.documents.Ticket;
import java.util.List;
import es.upm.miw.dtos.TicketModificationStateOrAmountDto;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class TicketControllerIT {

    @Autowired
    private TicketController ticketController;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private TicketModificationStateOrAmountDto ticketModificationStateOrAmountDto;
    private Article article;
    private Shopping shopping;
    private Ticket ticket;
    private Shopping[] shoppings;

    @BeforeEach
    void seed() {
        this.article = new Article();
        this.article.setCode("112287");
        this.articleRepository.save(this.article);
        this.shopping = new Shopping();
        this.shopping.setArticle(this.article);
        this.shoppings = new Shopping[1];
        this.shoppings[0] = this.shopping;
        this.ticket = new Ticket();
        this.ticket.setId("1395");
        this.ticket.setShoppingList(this.shoppings);
        this.ticketRepository.save(this.ticket);
    }

    @Test
    void testReadTicketById(){
        TicketModificationStateOrAmountDto ticketFound = ticketController.readTicketById("1395");
        assertNotNull(ticketFound);
        assertThrows(NotFoundException.class, () -> ticketController.readTicketById("Not exists"));

    }


}
