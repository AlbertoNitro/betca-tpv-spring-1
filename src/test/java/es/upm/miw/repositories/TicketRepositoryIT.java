package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.data_services.RandomTicketsBuilder;
import es.upm.miw.documents.Ticket;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class TicketRepositoryIT {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void testReadAll() {
        assertTrue(this.ticketRepository.findAll().size() > 0);
    }

    @Test
    void findByShoppingListArticle() {
        assertTrue(ticketRepository.findByShoppingListArticle("8400000000017").size() > 0);

        TicketRepository mockTicketRepository = Mockito.mock(TicketRepository.class);
        Mockito.when(mockTicketRepository.findByShoppingListArticle("8400000000017"))
                .thenReturn(RandomTicketsBuilder.randomTickets(articleRepository));
        List<Ticket> tickets = mockTicketRepository.findByShoppingListArticle("8400000000017");
        Mockito.verify(mockTicketRepository).findByShoppingListArticle("8400000000017");
        assertTrue(tickets.size() > 0);
    }
}
