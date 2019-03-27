package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.data_services.RandomTicketsBuilder;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Ticket;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.TicketRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@TestConfig
class StockPredictionControllerIT {

    TicketRepository mockTicketRepository;
    @Autowired
    private StockPredictionController controller;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void test() {
        mockTicketRepository = Mockito.mock(TicketRepository.class);
        Mockito.when(mockTicketRepository.findByShoppingListArticle("8400000000017"))
                .thenReturn(randomTickets());
        controller.setTicketRepository(mockTicketRepository);


        Mockito.verify(mockTicketRepository).findByShoppingListArticle("8400000000017");
    }

    private List<Ticket> randomTickets() {
        Article article8400000000017 = articleRepository.findById("8400000000017").orElse(null);
        Article article8400000000024 = articleRepository.findById("8400000000024").orElse(null);
        return new RandomTicketsBuilder().
                fromDate(LocalDateTime.now().minusMonths(1))
                .numberOfTickets(5)
                .addTicketArticle(article8400000000017, 3)
                .addTicketArticle(article8400000000024, 2)
                .build();
    }

}
