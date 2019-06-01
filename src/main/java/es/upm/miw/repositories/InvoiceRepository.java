package es.upm.miw.repositories;

import es.upm.miw.documents.Invoice;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.ProviderMinimunDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    public List<Invoice> findByUser(User user);
    public List<Invoice> findByCreationDateBetween(LocalDateTime afterDate, LocalDateTime beforeDate);
    public List<Invoice> findByUserAndCreationDateBetween(User user, LocalDateTime afterDate, LocalDateTime beforeDate);
    public List<Invoice> findByReferencesPositiveInvoice(String id);
}
