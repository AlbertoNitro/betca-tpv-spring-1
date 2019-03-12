package es.upm.miw.repositories;

import es.upm.miw.documents.Ticket;
import es.upm.miw.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    Ticket findFirstByOrderByCreationDateDescIdDesc();

    ArrayList<Ticket> findByUser(String userId);

}
