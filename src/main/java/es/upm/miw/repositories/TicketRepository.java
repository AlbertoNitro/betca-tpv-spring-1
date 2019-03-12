package es.upm.miw.repositories;

import es.upm.miw.documents.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    Ticket findFirstByOrderByCreationDateDescIdDesc();

    List<Ticket> findByUser(String userId);

}
