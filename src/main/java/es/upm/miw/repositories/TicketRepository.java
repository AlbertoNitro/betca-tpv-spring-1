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

    List<TicketQueryOutputDto> findByUser(String userId);

    @Query("{creationDate: {$gte: ?0, $lte: ?1}}")
    List<TicketQueryOutputDto> findByDateRange(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Ticket> findByCreationDateBetween(Date dateFrom, Date dateTo);

}
