package es.upm.miw.repositories;

import es.upm.miw.documents.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    Ticket findFirstByOrderByCreationDateDescIdDesc();

    Optional<Ticket> findByGiftTicket(String giftTicketId);

    List<Ticket> findByUser(String userId);

    List<Ticket> findByCreationDateBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Ticket> findByShoppingListArticle(String articleCode);

    List<Ticket> findByReference(String reference);

}
