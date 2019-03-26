package es.upm.miw.repositories;

import es.upm.miw.documents.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    Ticket findFirstByOrderByCreationDateDescIdDesc();

    List<Ticket> findByUser(String userId);

    List<Ticket> findByCreationDateBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Ticket> findByShoppingListArticle(String articleCode);
}
