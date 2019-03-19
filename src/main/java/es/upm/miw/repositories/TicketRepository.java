package es.upm.miw.repositories;

import es.upm.miw.documents.Ticket;
import es.upm.miw.dtos.output.TicketQueryOutputDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    Ticket findFirstByOrderByCreationDateDescIdDesc();

    List<Ticket> findByUser(String userId);

    List<Ticket> findByCreationDateBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

}
